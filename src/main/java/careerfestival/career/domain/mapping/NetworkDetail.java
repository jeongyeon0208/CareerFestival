package careerfestival.career.domain.mapping;

import careerfestival.career.domain.Record;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class NetworkDetail {

    // 인맥 네트워킹 이름
    @Column(name = "networking_name")
    private String networkingName;
    // 인맥 네트워킹 전화번호 및 이메일
    @Column(name = "networking_contact")
    private String networkingContact;

    @ManyToOne(fetch = FetchType.LAZY)
    private Record record;
}