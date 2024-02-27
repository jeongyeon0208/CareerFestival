package careerfestival.career.register.dto;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.enums.Category;
import careerfestival.career.domain.enums.KeywordName;
import careerfestival.career.domain.mapping.Region;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterEventDto {
    private Category category;
    private KeywordName keywordName;

    private LocalDateTime recruitmentStart;
    private LocalDateTime recruitmentEnd;

    private String eventName;
    private String description;

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    // 행사 신청 외부사이트
    private String link;
    private String eventContent;

    private String eventCost;

    private String address;
    private String specAddress;

    private String managerName;
    private String managerEmail;

    private String eventEtcDetail;

    // 지역 관련 컬럼
    private String city;
    private String addressLine;

    @Builder
    public Event toEventEntity() {
        return Event.builder()
                .category(category)
                .keywordName(keywordName)
                .recruitmentStart(recruitmentStart)
                .recruitmentEnd(recruitmentEnd)
                .eventName(eventName)
                .description(description)
                .eventStart(eventStart)
                .eventEnd(eventEnd)
                .link(link)
                .eventContent(eventContent)
                .eventCost(eventCost)
                .address(address)
                .specAddress(specAddress)
                .managerName(managerName)
                .managerEmail(managerEmail)
                .eventEtcDetail(eventEtcDetail)
                .build();
    }
    @Builder
    public Region toRegionEntity(){
        return Region.builder()
                .city(city)
                .addressLine(addressLine)
                .build();
    }
}
