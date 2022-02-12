package dev.besimgurbuz.backend.blog.clients;

import dev.besimgurbuz.backend.blog.dtos.MediumFeedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Component
public class MediumClient {
    private static final String TAG = "MediumClient";
    private static final Logger logger = Logger.getLogger(TAG);
    private final RestTemplate restTemplate;

    @Value("${medium.username}")
    private String username;


    MediumClient(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MediumFeedResponse getMediumFeed() {
        try {
            logger.log(Level.INFO, "Fetching blog post history via Medium feed.");
            final String feedUrl = "https://api.rss2json.com/v1/api.json?rss_url=https://medium.com/feed/@" + username;

            ResponseEntity<MediumFeedResponse> response = restTemplate.getForEntity(feedUrl, MediumFeedResponse.class);
            return response.getBody();
        } catch (RestClientException exception) {
            logger.log(Level.WARNING, "Couldn't fetched blog history via Medium feed: " + exception.getMessage());
            return null;
        }
    }
}
