package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@AllArgsConstructor
public class Album {
    private List<Artist> artists;
    private List<Image> images;
    @JsonProperty("externalUrls")
    private ExternalUrl externalUrl;
}
