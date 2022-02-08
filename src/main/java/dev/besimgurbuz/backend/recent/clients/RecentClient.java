package dev.besimgurbuz.backend.recent.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

/**
 * @author Besim Gurbuz
 */
public abstract class RecentClient<T> {
    RestTemplate restTemplate;

    RecentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    abstract public T getRecentActivity();
}
