package dev.besimgurbuz.backend.recent.controllers;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import dev.besimgurbuz.backend.recent.services.RecentActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/v1/recent")
public class RecentActivityController {
    private final RecentActivityService recentActivityService;
    private final SpotifyClient spotifyClient;

    RecentActivityController(@Autowired RecentActivityService recentActivityService,
                             @Autowired SpotifyClient spotifyClient) {
        this.recentActivityService = recentActivityService;
        this.spotifyClient = spotifyClient;
    }

    @GetMapping
    public RecentSteamActivity getRecentSteamActivity() {
        return recentActivityService.getRecentSteamActivity();
    }

    @GetMapping("/spotify")
    public PlayHistory[] recentSpotify() {
        return spotifyClient.getRecentActivity();
    }
}
