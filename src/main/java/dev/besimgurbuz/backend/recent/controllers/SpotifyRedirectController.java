package dev.besimgurbuz.backend.recent.controllers;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;

import java.io.IOException;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/spotify")
public class SpotifyRedirectController {
    private final SpotifyClient spotifyClient;

    SpotifyRedirectController(@Autowired SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("user-token")
    public String getSpotifyUserTokens(@RequestParam("code") String userCode) {
        AuthorizationCodeRequest authorizationCodeRequest = SpotifyClient.spotifyApi.authorizationCode(userCode)
                .build();

        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            SpotifyClient.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            SpotifyClient.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Access Token: " + authorizationCodeCredentials.getAccessToken()
                    + " Refresh Token: " + authorizationCodeCredentials.getRefreshToken()
                    + " Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return SpotifyClient.spotifyApi.getAccessToken();
    }

    @GetMapping("login")
    public String spotifyLogin() {
        return spotifyClient.spotifyLogin();
    }
}
