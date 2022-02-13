package dev.besimgurbuz.backend.repos.services;

import dev.besimgurbuz.backend.repos.clients.GithubClient;
import dev.besimgurbuz.backend.repos.dtos.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Besim Gurbuz
 */
@Service
public class GithubReposService {
    @Value("${github.username}")
    private String githubUsername;

    private final GithubClient githubClient;

    GithubReposService(@Autowired GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<GithubRepository> getGithubRepositories(Integer sliceStart, Integer sliceEnd) {
        List<GithubRepository> githubRepos = githubClient.getGithubRepos().stream().filter((repo) ->
                        !repo.isForked() && !repo.isDisabled() && !repo.getName().equals(githubUsername))
                .sorted((r1, r2) -> (int) (r2.getStarsCount() - r1.getStarsCount()))
                .collect(Collectors.toList());

        if (sliceStart != null && sliceEnd != null) {
            return githubRepos.subList(sliceStart, sliceEnd);
        } else if (sliceStart != null) {
            return githubRepos.subList(sliceStart, githubRepos.size());
        }

        return githubRepos;
    }
}
