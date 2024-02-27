package careerfestival.career.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    남성, 여성;

    @JsonCreator
    public static Gender fromString(String genderStr) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(genderStr)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No constant with text " + genderStr + " found");
    }
}