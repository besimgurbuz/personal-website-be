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
        Optional<SpotifyActivityResult> latestSpotifyActivity = Optional.ofNullable(spotifyActivity.getItems().get(0).getTrack())
                .map((track -> new SpotifyActivityResult(
                        track.getAlbum().getArtists().stream().map(Artist::getName).collect(Collectors.joining(", ")),
                        track.getAlbum().getName(),
                        track.getAlbum().getImages().get(2),
                        track.getAlbum().getExternalUrl().getSpotify())));
        Optional<SteamActivityResult> latestSteamActivity = Optional.ofNullable(steamActivity.getResponse().getGames()).map(games -> {
            Game latest = games.get(games.size() - 1);
            return new SteamActivityResult(latest.getName(), latest.getImgIconUrl());
        });

        spotify = latestSpotifyActivity.orElse(null);
        steam = latestSteamActivity.orElse(null);
    }
}
