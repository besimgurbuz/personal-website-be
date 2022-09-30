package dev.besimgurbuz.backend.skills.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.List;

/**
 * @author Besim Gurbuz
 */
@Data
public class Skill {
    private String label;
    private List<SkillType> types;
    private String iconUrl;
    private boolean inProgress;

    Skill() {
        this.label = "";
        this.types = List.of();
        this.iconUrl = "";
        this.inProgress = false;
    }

    enum SkillType {
        BACKEND("back-end"),
        FRONTEND("front-end"),
        DATABASE("database"),
        SYSTEM_PROGRAMMING("system programming");

        private final String label;

        SkillType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }

        @JsonCreator
        public static SkillType fromString(String str) {
            for (SkillType type : SkillType.values()) {
                if (type.label.equalsIgnoreCase(str) || type.name().equalsIgnoreCase(str)) {
                    return type;
                }
            }
            return null;
        }

        @JsonValue
        public String toValue() {
            return this.label;
        }
    }
}
