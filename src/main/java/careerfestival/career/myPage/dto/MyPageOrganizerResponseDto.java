package careerfestival.career.myPage.dto;

        import lombok.Builder;
        import lombok.Data;

@Builder
@Data
public class MyPageOrganizerResponseDto {
    private int countFollower;

    private int countEvent;
}
