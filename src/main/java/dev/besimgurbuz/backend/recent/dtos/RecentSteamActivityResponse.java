package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
public class RecentSteamActivityResponse {
    @JsonProperty("total_count")
    private Long totalCount;
    private List<Game> games;
}
