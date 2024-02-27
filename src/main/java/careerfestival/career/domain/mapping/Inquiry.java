package careerfestival.career.domain.mapping;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import careerfestival.career.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300, name = "comment_content")
    private String commentContent;

    private Long orderNumber;

    @Column(columnDefinition = "INT")
    private int depth;

    private boolean isParent;

    private boolean secret;

    private String secreteMessage;

    @Column(length = 300, name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_inquriy_id")
    private Inquiry parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Inquiry> childComments;



    public Inquiry(User user, Event event, Inquiry parent, Long orderNumber, int depth, String name, String secreteMessage, boolean secret){
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
        this.secret = secret;
        this.secreteMessage = secreteMessage; // 외부에서 설정된 값으로 초기화

    }
    public boolean isSecret() {
        return secret;
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
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
