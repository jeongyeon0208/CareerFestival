package careerfestival.career.domain.mapping;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Statistics {

    @Column(columnDefinition = "0")
    private int countedMale;

    @Column(columnDefinition = "0")
    private int countedFemale;

    @Column(columnDefinition = "0")
    int countedUnder19;
    @Column(columnDefinition = "0")
    int counted20to24;
    @Column(columnDefinition = "0")
    int counted25to29;
    @Column(columnDefinition = "0")
    int counted30to34;
    @Column(columnDefinition = "0")
    int counted35to39;
    @Column(columnDefinition = "0")
    int countedOver40;

    @Column(columnDefinition = "0")
    private int countedCompanyType1;
    @Column(columnDefinition = "0")
    private int countedCompanyType2;
    @Column(columnDefinition = "0")
    private int countedCompanyType3;
    @Column(columnDefinition = "0")
    private int countedCompanyType4;
    @Column(columnDefinition = "0")
    private int countedCompanyType5;
    @Column(columnDefinition = "0")
    private int countedCompanyType6;

}
