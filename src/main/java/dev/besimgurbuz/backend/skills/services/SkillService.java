package dev.besimgurbuz.backend.skills.services;

import dev.besimgurbuz.backend.skills.dtos.Skill;
import dev.besimgurbuz.backend.skills.repositories.SkillsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Comparator.comparing;

/**
 * @author Besim Gurbuz
 */
@Service
@RequiredArgsConstructor
public class SkillService {
   private final SkillsRepository repository;
   private static final String TAG = "SkillService";
   private static final Logger logger = Logger.getLogger(TAG);

   public List<Skill> getSkills() {
      try {
         List<Skill> skills = repository.getSkills();
         skills.sort(comparing(Skill::isInProgress).thenComparing(Skill::getLabel));
         return skills;
      } catch (IOException e) {
         logger.log(Level.INFO, "Failed to get skills: {}", e.getCause());
         return List.of();
      }
   }
}
