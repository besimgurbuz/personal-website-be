package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.dtos.SpotifyRefreshTokenResponse;
import dev.besimgurbuz.backend.recent.enums.SpotifyTokenKey;
import dev.besimgurbuz.backend.recent.utils.SpotifyTokenHandler;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author Besim Gurbuz
 */
@Component
@Lazy
public class SpotifyClient extends RecentClient<RecentSpotifyActivity> {
    @Value("${spotify.api_url}")
    private String apiUrl;

    @Value("${spotify.client_id}")
    private String clientId;

    @Value("${spotify.client_secret}")
    private String clientSecret;

    private static final URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080/api/spotify/user-token");
    private final String SPOTIFY_RECENTLY_PLAYED_PATH = "/me/player/recently-played";

    private SpotifyApi spotifyApi;
    private final SpotifyTokenHandler spotifyTokenHandler;

    SpotifyClient(@Autowired RestTemplate restTemplate,
                  @Autowired SpotifyTokenHandler spotifyTokenHandler) {
        super(restTemplate);
        this.spotifyTokenHandler = spotifyTokenHandler;
    }

    @PostConstruct()
    private void onInit() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();
    }

    @Override
    public RecentSpotifyActivity getRecentActivity() {
        HttpEntity<?> entity = new HttpEntity<>(getApiRequestHeaders());

        try {
            ResponseEntity<RecentSpotifyActivity> response = restTemplate.exchange(apiUrl + SPOTIFY_RECENTLY_PLAYED_PATH, HttpMethod.GET,
                    entity, RecentSpotifyActivity.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 401)  {
                refreshAccessToken();
                return getRecentActivity();
            }

            return null;
        }
    }

    public String getSpotifyLoginURI() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-recently-played")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    public Map<String, String> getTokensByExchangeCode(String exchangeCode) throws SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(exchangeCode)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Access Token: " + authorizationCodeCredentials.getAccessToken()
                    + " Refresh Token: " + authorizationCodeCredentials.getRefreshToken()
                    + " Expires in: " + authorizationCodeCredentials.getExpiresIn());
            return Map.ofEntries(
                    entry(SpotifyTokenKey.SPOTIFY_ACCESS_TOKEN.toString(), authorizationCodeCredentials.getAccessToken()),
                    entry(SpotifyTokenKey.SPOTIFY_REFRESH_TOKEN.toString(), authorizationCodeCredentials.getRefreshToken()));
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("ERROR: " + e.getMessage());
            throw new SpotifyWebApiException("Token couldn't fetched.");
        }
    }

    private HttpHeaders getApiRequestHeaders() {
        String accessToken = spotifyTokenHandler.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        return headers;
    }

    private void refreshAccessToken() {
        final String url = "https://accounts.spotify.com/api/token";
        String refreshToken = spotifyTokenHandler.getRefreshToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SpotifyRefreshTokenResponse> response = restTemplate
                .exchange(url, HttpMethod.POST, entity, SpotifyRefreshTokenResponse.class);
        SpotifyRefreshTokenResponse body = response.getBody();

        spotifyTokenHandler.setTokens(body.getAccessToken(), body.getRefreshToken().isEmpty()
                ? refreshToken : body.getRefreshToken());
    }
}
