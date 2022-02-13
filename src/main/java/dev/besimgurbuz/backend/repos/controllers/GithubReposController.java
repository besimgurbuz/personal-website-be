package dev.besimgurbuz.backend.repos.controllers;

import dev.besimgurbuz.backend.repos.dtos.GithubRepository;
import dev.besimgurbuz.backend.repos.services.GithubReposService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/v1/github")
public class GithubReposController {
    private final GithubReposService githubReposService;

    public GithubReposController(@Autowired GithubReposService githubReposService) {
        this.githubReposService = githubReposService;
    }

    @GetMapping
    public List<GithubRepository> getGithubRepositories(@RequestParam(defaultValue = "0") Integer sliceStart,
                                                        @RequestParam(required = false) Integer sliceEnd) {
        return githubReposService.getGithubRepositories(sliceStart, sliceEnd);
    }
}
