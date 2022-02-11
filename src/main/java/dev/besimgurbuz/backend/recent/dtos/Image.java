package dev.besimgurbuz.backend.recent.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class Image {
    private Long height;
    private String url;
    private Long width;
}
