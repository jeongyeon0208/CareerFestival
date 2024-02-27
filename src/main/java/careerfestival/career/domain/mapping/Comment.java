package careerfestival.career.domain.mapping;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, name = "comment_content")
    private String commentContent;

    private Long orderNumber;

    @Column(columnDefinition = "INT")
    private int depth;

    private boolean isParent;

    @Column(name = "total_like_count", columnDefinition = "int default 0")
    private Integer totalLikeCount;

    @Column(length = 300, name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Comment> childComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> like = new ArrayList<>();

    public Comment(User user, Event event, Comment parent, Long orderNumber, int depth, String name, Integer totalLikeCount){
        this.user = user;
        this.event = event;
        this.parent = parent;
        this.orderNumber = orderNumber;
        this.name = name;
        if (parent == null){
            this.isParent = false;
            this.depth = 0;
        }
        else {
            this.isParent = true;
            this.depth = parent.getDepth();

        }
        this.totalLikeCount = totalLikeCount;

    }

    public void  setIsParent(boolean isParent){
        this.isParent = isParent;
    }
    public void setOrderNumber(Long orderNumber) {

        this.orderNumber = orderNumber;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void  setName(String name){
        this.name = name;
    }

}
