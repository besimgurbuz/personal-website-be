package dev.besimgurbuz.backend.recent.services;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import dev.besimgurbuz.backend.recent.clients.SteamClient;
import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Besim Gurbuz
 */
@Service
public class RecentActivityService {
    private final SteamClient steamClient;
    private final SpotifyClient spotifyClient;

    RecentActivityService(@Autowired SteamClient steamClient, @Autowired SpotifyClient spotifyClient) {
        this.steamClient = steamClient;
        this.spotifyClient = spotifyClient;
    }

    public RecentSteamActivity getRecentSteamActivity() {
        return steamClient.getRecentActivity();
    }

    public RecentSpotifyActivity getRecentSpotifyActivity() {
        return spotifyClient.getRecentActivity();
    }
}
