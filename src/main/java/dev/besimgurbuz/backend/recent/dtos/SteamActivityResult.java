package dev.besimgurbuz.backend.recent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SteamActivityResult {
    private String gameName;
    private String iconUrl;
    private String storeUrl;
}
