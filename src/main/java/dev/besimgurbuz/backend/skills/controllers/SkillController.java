package dev.besimgurbuz.backend.skills.controllers;

import dev.besimgurbuz.backend.skills.dtos.Skill;
import dev.besimgurbuz.backend.skills.services.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@RestController
@RequestMapping("/api/v1/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/all")
    public List<Skill> getSkills() {
        return skillService.getSkills();
    }
}
