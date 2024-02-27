package careerfestival.career.register.dto;

import careerfestival.career.domain.mapping.Organizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterOrganizerDto {
    private String organizerName;

    @Builder
    public Organizer toEntity() {
        return Organizer.builder()
                .organizerName(organizerName)
                .build();
    }
}
