package dev.besimgurbuz.backend.repos.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
@NoArgsConstructor
public class GithubRepository {
    private long id;
    private String name;
    @JsonProperty("private")
    private boolean isPrivate;
    @JsonProperty("html_url")
    private String htmlURL;
    private String description;
    @JsonProperty("fork")
    private boolean isForked;
    @JsonProperty("stargazers_count")
    private long starsCount;
    @JsonProperty("forks_count")
    private long forksCount;
    private String language;
    @JsonProperty("archived")
    private boolean isArchived;
    @JsonProperty("disabled")
    private boolean isDisabled;
    private List<String> topics;
}
