package careerfestival.career.domain.mapping;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatisticsDto {

    private int countedMale;

    private int countedFemale;

    int countedUnder19;
    int counted20to24;
    int counted25to29;
    int counted30to34;
    int counted35to39;
    int countedOver40;

    private int countedCompanyType1;
    private int countedCompanyType2;
    private int countedCompanyType3;
    private int countedCompanyType4;
    private int countedCompanyType5;
    private int countedCompanyType6;
}
