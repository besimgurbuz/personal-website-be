package dev.besimgurbuz.backend.blog.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class MediumFeed {
    private String url;
    private String title;
    private String link;
    private String author;
    private String description;
    private String image;
}
