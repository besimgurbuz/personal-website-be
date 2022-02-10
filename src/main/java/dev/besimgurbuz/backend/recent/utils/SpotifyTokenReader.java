package dev.besimgurbuz.backend.recent.utils;

import dev.besimgurbuz.backend.recent.enums.SpotifyTokenKey;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Component
public class SpotifyTokenReader {

    public String getAccessToken() {
        try {
            return getTokens().get(SpotifyTokenKey.SPOTIFY_ACCESS_TOKEN.toString());
        } catch (IOException e) {
            return "";
        }
    }

    public String getRefreshToken() {
        try {
            return getTokens().get(SpotifyTokenKey.SPOTIFY_REFRESH_TOKEN.toString());
        } catch (IOException e) {
            return "";
        }
    }

    private Map<String, String> getTokens() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/data/token.txt"));
        return lines.stream().map(
                (line) -> line.split("=")
        ).collect(Collectors.toMap((keyValue) -> keyValue[0], (keyValue) -> keyValue[1]));
    }
}
