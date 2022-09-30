package dev.besimgurbuz.backend.skills.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.besimgurbuz.backend.skills.dtos.Skill;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Component
public class SkillsRepository {
    public List<Skill> getSkills() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(new File("src/main/resources/data/skills.json"), new TypeReference<>() {
        });
    }
}
