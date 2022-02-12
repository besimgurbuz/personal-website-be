package dev.besimgurbuz.backend.recent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
public class SteamActivityResult {
    private String gameName;
    private String iconUrl;
}
