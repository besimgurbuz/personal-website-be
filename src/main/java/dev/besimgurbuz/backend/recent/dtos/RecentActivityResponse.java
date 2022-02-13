package dev.besimgurbuz.backend.recent.dtos;

import lombok.Data;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Data
public class RecentActivityResponse {
    SpotifyActivityResult spotify;
    SteamActivityResult steam;

    public RecentActivityResponse(RecentSpotifyActivity spotifyActivity, RecentSteamActivity steamActivity) {
        Track latestTrack = spotifyActivity.getItems().get(0).getTrack();
        Optional<SteamActivityResult> latestGame = Optional.ofNullable(steamActivity.getResponse().getGames()).map(games -> {
            Game latest = games.get(0);
            return new SteamActivityResult(latest.getName(), latest.getImgIconUrl());
        });
        spotify = new SpotifyActivityResult(
                latestTrack.getAlbum().getArtists().stream().map(Artist::getName).collect(Collectors.joining(", ")),
                latestTrack.getAlbum().getName(),
                latestTrack.getAlbum().getImages().get(2),
                latestTrack.getAlbum().getExternalUrl().getSpotify());
        steam = latestGame.orElse(null);
    }
}
