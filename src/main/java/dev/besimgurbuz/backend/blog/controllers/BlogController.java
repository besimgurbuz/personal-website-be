package dev.besimgurbuz.backend.blog.controllers;

import dev.besimgurbuz.backend.blog.dtos.MediumFeedResponse;
import dev.besimgurbuz.backend.blog.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {
    private final BlogService blogService;

    BlogController(@Autowired BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public MediumFeedResponse getBlogHistory() {
        return blogService.getMediumFeed();
    }
}
