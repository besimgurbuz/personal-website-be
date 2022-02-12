package dev.besimgurbuz.backend.blog.services;

import dev.besimgurbuz.backend.blog.clients.MediumClient;
import dev.besimgurbuz.backend.blog.dtos.MediumFeedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Besim Gurbuz
 */
@Service
public class BlogService {
    private final MediumClient mediumClient;

    BlogService(@Autowired MediumClient mediumClient) {
        this.mediumClient = mediumClient;
    }

    public MediumFeedResponse getMediumFeed() {
        Optional<MediumFeedResponse> mediumFeed = Optional.ofNullable(mediumClient.getMediumFeed());

        return mediumFeed.orElse(new MediumFeedResponse());
    }
}
