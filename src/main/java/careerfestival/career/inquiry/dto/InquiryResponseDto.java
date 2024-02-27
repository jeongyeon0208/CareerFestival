package careerfestival.career.inquiry.dto;

import careerfestival.career.domain.mapping.Inquiry;
import careerfestival.career.repository.CommentLikeRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryResponseDto {
    private Long userId;
    private Long eventId;
    private String commentContent;
    private Long parent;
    private String Name;
    private String secreteMessage; // 비밀 댓글 여부에 따른 메시지
    private boolean secret;


    public void setSecreteMessage(String secreteMessage) {
        this.secreteMessage = secreteMessage;
    }
    public void setCommentContent(String commentContent){ this.commentContent = commentContent;}
    public InquiryResponseDto(Inquiry inquiry) {
        this.userId = (inquiry.getUser() != null) ? inquiry.getUser().getId() : null;
        this.eventId = (inquiry.getEvent() != null) ? inquiry.getEvent().getId() : null;
        this.commentContent = inquiry.getCommentContent();
        this.parent = (inquiry.getParent() != null) ? inquiry.getParent().getId() : null;
        this.Name = inquiry.getName();
        this.secreteMessage = inquiry.isSecret() ? getSecreteMessage() : null;
        this.secret = inquiry.isSecret();
    }
}
