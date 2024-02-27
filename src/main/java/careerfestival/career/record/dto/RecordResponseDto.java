package careerfestival.career.record.dto;

import careerfestival.career.domain.Record;
import careerfestival.career.domain.enums.Category;
import careerfestival.career.domain.enums.KeywordName;
import careerfestival.career.domain.mapping.NetworkDetail;
import careerfestival.career.domain.mapping.RecordDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordResponseDto {
    private Category category;
    private String eventName;
    private String eventTitle;
    private LocalDate eventDate;

    private List<KeywordName> keywordName;

    private List<RecordDetail> recordDetails;
    private List<NetworkDetail> networkDetails;

    @Builder
    public static RecordResponseDto mainFromEntity(Record record){
        return RecordResponseDto.builder()
                .category(record.getCategory())
                .eventName(record.getEventName())
                .eventTitle(record.getEventTitle())
                .eventDate(record.getEventDate())
                .keywordName(record.getKeywordName())
                .build();
    }

    // 각 게시판을 불러올 때 사용함
    @Builder
    public static RecordResponseDto recordFromEntity(Record record){
        return RecordResponseDto.builder()
                .category(record.getCategory())
                .eventName(record.getEventName())
                .eventTitle(record.getEventTitle())
                .eventDate(record.getEventDate())
                .keywordName(record.getKeywordName())
                .recordDetails(record.getRecordDetails())
                .networkDetails(record.getNetworkDetails())
                .build();
    }
}
