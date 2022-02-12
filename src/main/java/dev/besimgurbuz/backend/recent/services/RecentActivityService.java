package dev.besimgurbuz.backend.recent.services;

import dev.besimgurbuz.backend.recent.clients.SpotifyClient;
import dev.besimgurbuz.backend.recent.clients.SteamClient;
import dev.besimgurbuz.backend.recent.dtos.RecentActivityResponse;
import dev.besimgurbuz.backend.recent.dtos.RecentSpotifyActivity;
import dev.besimgurbuz.backend.recent.dtos.RecentSteamActivity;
import dev.besimgurbuz.backend.recent.exceptions.SpotifyAccessTokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Service
public class RecentActivityService {
    private static final String TAG = "RecentActivityService";
    private static final Logger logger = Logger.getLogger(TAG);

    private final SteamClient steamClient;
    private final SpotifyClient spotifyClient;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    RecentActivityService(@Autowired SteamClient steamClient, @Autowired SpotifyClient spotifyClient) {
        this.steamClient = steamClient;
        this.spotifyClient = spotifyClient;
    }

    public RecentActivityResponse getRecentActivities() {
        Future<RecentSpotifyActivity> spotifyRecentActivityFuture = executorService.submit(spotifyClient::getRecentActivity);
        Future<RecentSteamActivity> steamRecentActivityFuture = executorService.submit(steamClient::getRecentActivity);

        try {
            logger.log(Level.INFO, "Fetching recent activities with multiple threads.");

            RecentSpotifyActivity recentSpotifyActivity = spotifyRecentActivityFuture.get();
            RecentSteamActivity recentSteamActivity = steamRecentActivityFuture.get();

            return new RecentActivityResponse(recentSpotifyActivity, recentSteamActivity);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't fetched recent activities with multiple threads.");
            return getRecentActivitiesSynchronously();
        }
    }

    public RecentSpotifyActivity getRecentSpotifyActivity() throws SpotifyAccessTokenExpiredException {
        try {
            logger.log(Level.INFO, "Fetching recent Spotify activity.");

            return spotifyClient.getRecentActivity();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't fetched recent Spotify activity.");
            return null;
        }
    }

    public RecentSteamActivity getRecentSteamActivity() {
        try {
            logger.log(Level.INFO, "Fetching recent Steam activity.");

            return steamClient.getRecentActivity();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't fetched recent Steam activity.");
            return null;
        }
    }

    private RecentActivityResponse getRecentActivitiesSynchronously() {
        try {
            logger.log(Level.INFO, "Trying to get recent activities on main thread only.");

            RecentSpotifyActivity spotifyActivity = spotifyClient.getRecentActivity();
            RecentSteamActivity steamActivity = steamClient.getRecentActivity();

            return new RecentActivityResponse(spotifyActivity, steamActivity);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't fetched recent activities even on main thread.");
            return null;
        }
    }
}
