package dev.besimgurbuz.backend.recent.controllers;

import dev.besimgurbuz.backend.recent.services.SpotifyAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/spotify")
@Profile("!prod")
public class SpotifyRedirectController {
    private final SpotifyAuthenticationService spotifyAuthenticationService;

    SpotifyRedirectController(@Autowired SpotifyAuthenticationService spotifyAuthenticationService) {
        this.spotifyAuthenticationService = spotifyAuthenticationService;
    }

    @GetMapping("login")
    public String spotifyLogin() {
        return spotifyAuthenticationService.getSpotifyLoginURI();
    }

    @GetMapping("user-token")
    public boolean authenticateSpotifyUser(@RequestParam("code") String exchangeCode) {
        return spotifyAuthenticationService.authenticateToSpotify(exchangeCode);
    }
}
