package dev.besimgurbuz.backend.recent.utils;

import dev.besimgurbuz.backend.recent.enums.SpotifyTokenKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Component
public class SpotifyTokenHandler {

    @Value("${spotify.access_token:NO_TOKEN}")
    private String accessToken;

    @Value("${spotify.refresh_token:NO_TOKEN}")
    private String refreshToken;

    @PostConstruct
    public void onInit() {
        // set tokens from env on initialization
        if (!accessToken.equals("NO_TOKEN") && !refreshToken.equals("NO_TOKEN")) {
            setTokens(accessToken, refreshToken);
        }
    }

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

    public void setTokens(Map<String, String> tokens) {
        setTokens(tokens.get(SpotifyTokenKey.SPOTIFY_ACCESS_TOKEN.toString()),
                tokens.get(SpotifyTokenKey.SPOTIFY_REFRESH_TOKEN.toString()));
    }

    public void setTokens(String accessToken, String refreshToken) {
        List<String> lines = new ArrayList<>();
        lines.add(SpotifyTokenKey.SPOTIFY_ACCESS_TOKEN + "=" + accessToken);
        lines.add(SpotifyTokenKey.SPOTIFY_REFRESH_TOKEN + "=" + refreshToken);

        try {
            Files.write(
                    Paths.get("src/main/resources/data/token.txt"),
                    lines,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Couldn't saved the tokens");
        }
    }


    private Map<String, String> getTokens() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/data/token.txt"));
        return lines.stream().map(
                (line) -> line.split("=")
        ).collect(Collectors.toMap((keyValue) -> keyValue[0], (keyValue) -> keyValue[1]));
    }
}
