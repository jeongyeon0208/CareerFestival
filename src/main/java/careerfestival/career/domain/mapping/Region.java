package careerfestival.career.domain.mapping;

import careerfestival.career.domain.Event;
import careerfestival.career.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "address_line")
    private String addressLine;

    @OneToOne(mappedBy = "region", cascade = CascadeType.ALL)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}