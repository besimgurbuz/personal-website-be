package dev.besimgurbuz.backend.recent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
public class SpotifyActivityResult {
    private String artistName;
    private String albumName;
    private Image image;
    private String url;
}
