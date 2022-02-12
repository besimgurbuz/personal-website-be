package dev.besimgurbuz.backend.recent.exceptions;

/**
 * @author Besim Gurbuz
 */
public class SpotifyAccessTokenExpiredException extends RuntimeException {
    public SpotifyAccessTokenExpiredException() {
        super("Used token while fetching recent activity on Spotify has expired.");
    }
}
