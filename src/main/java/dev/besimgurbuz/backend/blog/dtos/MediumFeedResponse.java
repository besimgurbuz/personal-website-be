package dev.besimgurbuz.backend.blog.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class MediumFeedResponse {
    private String status;
    private MediumFeed feed;
    private List<BlogItem>  items;
}
