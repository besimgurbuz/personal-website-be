package dev.besimgurbuz.backend.recent.services;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import dev.besimgurbuz.backend.recent.utils.SpotifyTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Service
public class SpotifyAuthenticationService {
    private final SpotifyClient spotifyClient;
    private final SpotifyTokenHandler tokenHandler;

    SpotifyAuthenticationService(@Autowired SpotifyClient spotifyClient,
                                 @Autowired SpotifyTokenHandler tokenHandler) {
        this.spotifyClient = spotifyClient;
        this.tokenHandler = tokenHandler;
    }

    public String getSpotifyLoginURI() {
        return spotifyClient.getSpotifyLoginURI();
    }

    public boolean authenticateToSpotify(String exchangeCode) {
        try {
            Map<String, String> tokens = spotifyClient.getTokensByExchangeCode(exchangeCode);
            saveTokens(tokens);
            return true;
        } catch (SpotifyWebApiException | URISyntaxException | IOException e) {
            return false;
        }
    }

    private void saveTokens(Map<String, String> tokens) throws URISyntaxException, IOException {
        tokenHandler.setTokens(tokens);
    }
}
