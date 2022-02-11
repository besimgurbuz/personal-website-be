package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

/**
 * @author Besim Gurbuz
 */
@Data
public class SpotifyRefreshTokenResponse {
    @JsonProperty("access_token")
    @NonNull
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    SpotifyRefreshTokenResponse() {
        refreshToken = "";
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = Objects.requireNonNullElse(refreshToken, "");
    }
}
