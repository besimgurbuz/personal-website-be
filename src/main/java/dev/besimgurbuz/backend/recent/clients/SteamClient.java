package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Component
public class SteamClient extends RecentClient<RecentSteamActivity> {
    private static final String TAG = "SteamClient";
    private static final Logger logger = Logger.getLogger(TAG);

    @Value("${steam.api_url}")
    private String clientBaseURL;
    @Value("${steam.auth_key}")
    private String clientAuthKey;
    @Value("${steam.user_id}")
    private String steamUserId;

    private final String RECENT_ACTIVITY_GAMING_PATH = "/IPlayerService/GetRecentlyPlayedGames/v0001?key=%s&steamid=%s&format=json";

    SteamClient(@Autowired RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public RecentSteamActivity getRecentActivity() {
        String recentActivityURL = String.format(clientBaseURL + RECENT_ACTIVITY_GAMING_PATH, clientAuthKey, steamUserId);

        ResponseEntity<RecentSteamActivity> recentSteamActivityResponseEntity =
                restTemplate.getForEntity(recentActivityURL, RecentSteamActivity.class);
        if (recentSteamActivityResponseEntity.getStatusCode().is2xxSuccessful()) {
            logger.log(Level.INFO, "Successfully fetched recent activity on Steam.");
            return recentSteamActivityResponseEntity.getBody();
        } else {
            return null;
        }
    }
}
