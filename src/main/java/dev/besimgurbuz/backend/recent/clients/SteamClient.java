package dev.besimgurbuz.backend.recent.clients;

import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Besim Gurbuz
 */
@Component
public class SteamClient extends RecentClient<RecentSteamActivity> {
    @Value("${steam.api_url}")
    private String clientBaseURL;
    @Value("${steam.auth_key}")
    private String clientAuthKey;
    @Value("${steam.user_id}")
    private String steamUserId;

    private final String recentGameActivityPath = "/IPlayerService/GetRecentlyPlayedGames/v0001?key=%s&steamid=%s&format=json";

    SteamClient(@Autowired RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public RecentSteamActivity getRecentActivity() {
        String recentActivityURL = String.format(clientBaseURL + recentGameActivityPath, clientAuthKey, steamUserId);

        ResponseEntity<RecentSteamActivity> recentSteamActivityResponseEntity =
                restTemplate.getForEntity(recentActivityURL, RecentSteamActivity.class);
        if (recentSteamActivityResponseEntity.getStatusCode().is2xxSuccessful()) {
            return recentSteamActivityResponseEntity.getBody();
        } else {
            return null;
        }
    }
}
