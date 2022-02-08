package dev.besimgurbuz.backend.recent.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Besim Gurbuz
 */
@Component
public class SpotifyClient extends RecentClient<Object> {
    SpotifyClient(@Autowired RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public Object getRecentActivity() {
        return null;
    }
}
