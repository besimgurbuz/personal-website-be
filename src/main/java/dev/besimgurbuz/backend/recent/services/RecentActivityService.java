package dev.besimgurbuz.backend.recent.services;

import dev.besimgurbuz.backend.recent.clients.SteamClient;
import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Besim Gurbuz
 */
@Service
public class RecentActivityService {
    private final SteamClient steamClient;

    RecentActivityService(@Autowired SteamClient steamClient) {
        this.steamClient = steamClient;
    }

    public RecentSteamActivity getRecentSteamActivity() {
        return steamClient.getRecentActivity();
    }
}
