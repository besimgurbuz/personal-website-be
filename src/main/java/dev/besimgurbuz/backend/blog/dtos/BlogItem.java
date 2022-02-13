package dev.besimgurbuz.backend.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class BlogItem {
    private String title;
    @JsonProperty("pubDate")
    private String publishDate;
    private String link;
    private String guid;
    private List<String> categories;
}
