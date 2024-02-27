package careerfestival.career.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CompanyType {

    미성년자, 학부생, 졸업생, 대학원생, 무직, 기타;

    @JsonCreator
    public static CompanyType fromString(String companyStr) {
        for (CompanyType companyType : CompanyType.values()) {
            if (companyType.name().equalsIgnoreCase(companyStr)) {
                return companyType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + companyStr + " found");
    }
}
