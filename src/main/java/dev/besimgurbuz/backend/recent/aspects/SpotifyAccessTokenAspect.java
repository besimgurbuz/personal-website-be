package dev.besimgurbuz.backend.recent.aspects;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.dtos.SpotifyRefreshTokenResponse;
import dev.besimgurbuz.backend.recent.exceptions.SpotifyAccessTokenExpiredException;
import dev.besimgurbuz.backend.recent.utils.SpotifyTokenHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Aspect
@Component
public class SpotifyAccessTokenAspect {
    private static final String TAG = "SpotifyAccessTokenAspect";
    private static final Logger logger = Logger.getLogger(TAG);

    private final RestTemplate restTemplate;
    private final SpotifyTokenHandler tokenHandler;

    SpotifyAccessTokenAspect(@Autowired SpotifyTokenHandler tokenHandler, @Autowired RestTemplate restTemplate) {
        this.tokenHandler = tokenHandler;
        this.restTemplate = restTemplate;
    }


    @Around("execution(* dev.besimgurbuz.backend.recent.clients.SpotifyClient.getRecentActivity())")
    public RecentSpotifyActivity getActivityWithRefreshedToken(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            return (RecentSpotifyActivity) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            if (e instanceof SpotifyAccessTokenExpiredException) {
                SpotifyClient client = (SpotifyClient) proceedingJoinPoint.getTarget();
                String refreshToken = tokenHandler.getRefreshToken();
                ResponseEntity<SpotifyRefreshTokenResponse> refreshTokenResponse =
                        postRefreshTokenRequest(client.id, client.secret, refreshToken);
                SpotifyRefreshTokenResponse body = refreshTokenResponse.getBody();

                if (body != null) {
                    logger.log(Level.INFO, "Successfully fetched new active access token for Spotify API.");
                    tokenHandler.setTokens(body.getAccessToken(), body.getRefreshToken().isEmpty()
                            ? refreshToken : body.getRefreshToken());
                    return client.getRecentActivity();
                }
            }
            throw e;
        }
    }

    private ResponseEntity<SpotifyRefreshTokenResponse> postRefreshTokenRequest(String clientId,
                                                                                String clientSecret,
                                                                                String refreshToken) {
        try {
            final String url = "https://accounts.spotify.com/api/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("grant_type", "refresh_token");
            requestBody.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

            return restTemplate
                    .exchange(url, HttpMethod.POST, entity, SpotifyRefreshTokenResponse.class);
        } catch (RestClientException exception) {
            logger.log(Level.WARNING, "Error while trying to refresh spotify token: "
                    + exception.getMessage());
            return ResponseEntity.of(Optional.empty());
        }
    }
}
