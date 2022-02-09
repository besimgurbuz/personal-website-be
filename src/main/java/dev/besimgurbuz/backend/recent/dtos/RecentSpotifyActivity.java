package dev.besimgurbuz.backend.recent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
public class RecentSpotifyActivity {
    private List<SpotifyActivityItem> items;
}
