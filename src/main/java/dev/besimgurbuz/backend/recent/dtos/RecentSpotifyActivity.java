package dev.besimgurbuz.backend.recent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecentSpotifyActivity {
    private List<SpotifyActivityItem> items;
}
