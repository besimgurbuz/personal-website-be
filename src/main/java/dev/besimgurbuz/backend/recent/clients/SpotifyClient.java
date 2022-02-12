package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.enums.SpotifyTokenKey;
import dev.besimgurbuz.backend.recent.exceptions.SpotifyAccessTokenExpiredException;
import dev.besimgurbuz.backend.recent.utils.SpotifyTokenHandler;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Map.entry;

/**
 * @author Besim Gurbuz
 */
@Component
@Lazy
public class SpotifyClient extends RecentClient<RecentSpotifyActivity> {
    private static final String TAG = "SpotifyClient";
    private static final Logger logger = Logger.getLogger(TAG);
    private static final URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080/api/spotify/user-token");
    private final String SPOTIFY_RECENTLY_PLAYED_PATH = "/me/player/recently-played?limit=1";
    private final SpotifyTokenHandler spotifyTokenHandler;

    @Value("${spotify.client_id}")
    public String id;

    @Value("${spotify.client_secret}")
    public String secret;

    @Value("${spotify.api_url}")
    private String apiUrl;

    private SpotifyApi spotifyApi;

    SpotifyClient(@Autowired RestTemplate restTemplate,
                  @Autowired SpotifyTokenHandler spotifyTokenHandler) {
        super(restTemplate);
        this.spotifyTokenHandler = spotifyTokenHandler;
    }

    @PostConstruct()
    private void onInit() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(id)
                .setClientSecret(secret)
                .setRedirectUri(redirectURI)
                .build();
    }

    @Override
    public RecentSpotifyActivity getRecentActivity() throws SpotifyAccessTokenExpiredException {
        try {
            HttpEntity<?> entity = new HttpEntity<>(getApiRequestHeaders());
            ResponseEntity<RecentSpotifyActivity> response = restTemplate.exchange(apiUrl + SPOTIFY_RECENTLY_PLAYED_PATH, HttpMethod.GET,
                    entity, RecentSpotifyActivity.class);
            logger.log(Level.INFO, "Successfully fetched recent activity on Spotify.");
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 401)  {
                logger.log(Level.INFO, "Used token while fetching recent activity on Spotify has expired.");

                throw new SpotifyAccessTokenExpiredException();
            }
            logger.log(Level.WARNING, "Couldn't fetched recent activity for spotify: " + e.getMessage());
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
            logger.log(Level.WARNING, "Couldn't fetched authentication tokens for spotify: " + e.getMessage());
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
}
