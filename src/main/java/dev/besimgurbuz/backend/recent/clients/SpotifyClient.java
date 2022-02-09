package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.*;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERefreshRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Component
@Lazy
public class SpotifyClient extends RecentClient<PlayHistory[]> {
    @Value("${spotify.client_id}")
    private String clientId;

    @Value("${spotify.client_secret}")
    private String clientSecret;

    @Value("${spotify.access_token}")
    private String accessToken;

    @Value("${spotify.refresh_token}")
    private String refreshToken;

    private static final URI redirectURI = SpotifyHttpManager.makeUri("http://localhost:8080/api/spotify/user-token");
    public static SpotifyApi spotifyApi;

    SpotifyClient(@Autowired RestTemplate restTemplate) {
        super(restTemplate);
    }

    @PostConstruct
    void onInit() {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectURI)
                .build();
        if (accessToken != null && !accessToken.isEmpty()) {
            spotifyApi.setAccessToken(accessToken);
            spotifyApi.setRefreshToken(refreshToken);
        }


    }

    @Override
    public PlayHistory[] getRecentActivity() {
        final GetCurrentUsersRecentlyPlayedTracksRequest recentlyPlayedTracksRequest = spotifyApi
                .getCurrentUsersRecentlyPlayedTracks()
                    .limit(1)
                    .build();

        try {
            final PagingCursorbased<PlayHistory> playHistory = recentlyPlayedTracksRequest.execute();

            return playHistory.getItems();
        }  catch (UnauthorizedException exception) {
            refreshAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return new PlayHistory[]{};
    }

    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-recently-played")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    private void refreshAccessToken() {
        final AuthorizationCodePKCERefreshRequest authorizationCodePKCERefreshRequest = spotifyApi
                .authorizationCodePKCERefresh()
                .build();

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodePKCERefreshRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
    }
}
