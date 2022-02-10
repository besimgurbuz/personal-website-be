package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.enums.SpotifyTokenKey;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
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
    @Value("${spotify.client_id}")
    private String clientId;

    @Value("${spotify.client_secret}")
    private String clientSecret;

    @Value("${spotify.access_token}")
    private String accessToken;

    @Value("${spotify.refresh_token}")
    private String refreshToken;

    private static final URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080/api/spotify/user-token");
    public SpotifyApi spotifyApi;

    SpotifyClient(@Autowired RestTemplate restTemplate) {
        super(restTemplate);
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
        return null;
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

    private void refreshAccessToken() {
    }
}
