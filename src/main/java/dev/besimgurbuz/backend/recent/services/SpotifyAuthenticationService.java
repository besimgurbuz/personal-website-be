package dev.besimgurbuz.backend.recent.services;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
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

    SpotifyAuthenticationService(@Autowired SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
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
        List<String> lines = tokens.entrySet().stream().map((entry) -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList());
        Files.write(
                Paths.get("src/main/resources/data/token.txt"),
                lines,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
