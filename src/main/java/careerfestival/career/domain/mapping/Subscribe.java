package careerfestival.career.domain.mapping;

import careerfestival.career.domain.User;
import careerfestival.career.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscribe extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  // default EAGER
    @JoinColumn(name = "to_user")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    public Subscribe(User toUser, User fromUser){

        this.toUser = toUser;
        this.fromUser = fromUser;
    }
    public Long getFromUser() {
        return fromUser.getId();
    }

    public Long getToUser() {
        return toUser.getId();
    }

}
