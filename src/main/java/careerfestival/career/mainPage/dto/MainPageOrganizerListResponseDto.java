package careerfestival.career.mainPage.dto;

import careerfestival.career.domain.mapping.Organizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageOrganizerListResponseDto {
    private String organizerName;
    private String organizerProfileFileUrl;
    private int countEvent;

    public static MainPageOrganizerListResponseDto fromOrganizerEntity(Organizer organizer){
        return MainPageOrganizerListResponseDto
                .builder()
                .organizerName(organizer.getOrganizerName())
                .organizerProfileFileUrl(organizer.getOrganizerProfileFileUrl())
                .countEvent(organizer.getCountEvent())
                .build();
    }
}
