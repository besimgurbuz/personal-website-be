package dev.besimgurbuz.backend.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class BlogItem {
    private String title;
    @JsonProperty("pubDate")
    private OffsetDateTime publishDate;
    private String link;
    private String guid;
    private String author;
    private String thumbnail;
    private String description;
    private String content;
    private List<String> categories;
}
