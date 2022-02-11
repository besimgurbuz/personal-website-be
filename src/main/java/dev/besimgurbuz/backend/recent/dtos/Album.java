package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Album {
    private String name;
    private List<Artist> artists;
    private List<Image> images;
    @JsonProperty("external_urls")
    private ExternalUrl externalUrl;
}
