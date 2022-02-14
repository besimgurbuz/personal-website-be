package dev.besimgurbuz.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Besim Gurbuz
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static final String TAG = "WebConfig";
    private static final Logger logger = Logger.getLogger(TAG);

    @Value("${cors.allowed_origin}")
    private String allowedOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.log(Level.INFO, "Allowing all requests from origin: {0}", allowedOrigin);
        registry.addMapping("/**").allowedOrigins(allowedOrigin);
    }
}
