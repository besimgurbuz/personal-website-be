package dev.besimgurbuz.backend.recent.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Besim Gurbuz
 */
@Data
public class Game {
    private static final String STEAM_MEDIA_URL = "https://media.steampowered.com/steamcommunity/public/images/apps/%s/%s.jpg";
    private static final String STEAM_STORE_URL = "https://store.steampowered.com/app/%s";

    @JsonProperty("appid")
    private Long appId;
    private String name;
    @JsonProperty("img_icon_url")
    private String imgIconUrl;
    @JsonProperty("img_logo_url")
    private String imgLogoUrl;
    @JsonIgnore
    private String storeUrl;

    Game() {}

    Game(Long appId, String name, String imgIconURL, String imgLogoURL) {
        this.appId = appId;
        this.name = name;
        this.imgIconUrl =  String.format(STEAM_MEDIA_URL, appId, imgIconURL);
        this.imgLogoUrl = String.format(STEAM_MEDIA_URL, appId, imgLogoURL);
        this.storeUrl = String.format(STEAM_STORE_URL, appId);
    }

    public void setAppId(Long appId) {
        this.appId = appId;
        this.storeUrl = String.format(STEAM_STORE_URL, appId);
    }

    public void setImgIconUrl(String imgIconUrl) {
        if (appId != null) {
            this.imgIconUrl = String.format(STEAM_MEDIA_URL, appId, imgIconUrl);
        } else {
            this.imgIconUrl = imgIconUrl;
        }
    }

    public void setImgLogoUrl(String imgLogoUrl) {
        if (appId != null) {
            this.imgLogoUrl = String.format(STEAM_MEDIA_URL, appId, imgLogoUrl);
        } else {
            this.imgLogoUrl = imgLogoUrl;
        }
    }
}
