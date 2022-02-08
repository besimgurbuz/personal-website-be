package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Besim Gurbuz
 */
@Data
public class Game {
    @JsonProperty("appid")
    private Long appId;
    private String name;
    @JsonProperty("playtime_2weeks")
    private Long playtime2Weeks;
    @JsonProperty("playtime_forever")
    private Long playtimeForever;
    @JsonProperty("img_icon_url")
    private String imgIconURL;
    @JsonProperty("img_logo_url")
    private String imgLogoURL;
}
