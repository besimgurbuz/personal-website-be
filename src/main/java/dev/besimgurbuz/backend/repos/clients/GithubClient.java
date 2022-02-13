package dev.besimgurbuz.backend.repos.clients;

import dev.besimgurbuz.backend.repos.dtos.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Component
public class GithubClient {
    private static final String TAG = "GithubClient";
    private static final Logger logger = Logger.getLogger(TAG);
    private final RestTemplate restTemplate;

    @Value("${github.access_token}")
    private String accessToken;

    @Value("${github.username}")
    private String githubUsername;

    GithubClient(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<GithubRepository> getGithubRepos() {
        final String url = "https://api.github.com/users/" + githubUsername + "/repos?sort=stargazers&direction=asc";

        try {
            logger.log(Level.INFO, "Fetching GitHub repositories for {0}.", githubUsername);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<GithubRepository[]> response = restTemplate
                    .exchange(url, HttpMethod.GET, entity, GithubRepository[].class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.log(Level.INFO, "Successfully fetched GitHub repositories for {0}.", githubUsername);
                GithubRepository[] body = response.getBody();

                return Arrays.asList(Objects.requireNonNullElse(body, new GithubRepository[]{}));
            } else {
                logger.log(Level.WARNING, "Faced non-ok response while fetching GitHub repositories for {0}: " + response.getStatusCodeValue(), githubUsername);
                return List.of();
            }
        } catch (RestClientException exception) {
            logger.log(Level.WARNING, "Couldn't fetched repos: " + exception.getMessage());
            return List.of();
        }
    }
}
