package careerfestival.career.domain;

import careerfestival.career.domain.common.BaseEntity;
import careerfestival.career.domain.enums.*;
import careerfestival.career.domain.mapping.*;
import careerfestival.career.myPage.dto.UpdateMypageResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class User extends BaseEntity {

    // 회원가입 1, 2 화면
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, name = "name")
    private String name;

    @Column(nullable = false, length = 300, name = "password")
    private String password;

    @Email
    @Column(unique = true, nullable = false, length = 300, name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 전화번호는 회원가입 3 화면에서 저장되는 값이기 때문에 nullable이면 insert 과정에서 오류 발생 -> null 허용으로 수정함
    @Column(length = 200, name = "phone_number")
    private String phoneNumber;

    // 회원가입 3 화면 (대략 6가지)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "INT")
    private int age;

    // 소속
    @Enumerated(EnumType.STRING)
    private CompanyType company;

    // 부서 및 학과
    @Column(length = 20, name = "department")
    private String department;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<KeywordName> keywordName = new ArrayList<>();

    @Column(name = "user_profile_file_url")
    private String userProfilefileUrl;

    private Timestamp inactiveDate;

    // status와 inactivedate는 회원 탈퇴, 게시글 삭제 시 필요 기능
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Inquiry> inquiry = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> event = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> record = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wish = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Participate> participate = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Organizer> organizer = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Region> region = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL)
    private List<Subscribe> subscribe = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLike> commentLike = new ArrayList<>();

  
    public void addRecord(Record record) {
        records.add(record);
    }
    public void setUserStatus() {
        this.userStatus = UserStatus.ACTIVE;
    }

    //--------------------------------update--------------------------------

    public void updatePassword(String password) {
        if(password==null) return;
        this.password = password;
    }

    public void updateName(String name) {
        if(name==null) return;
        this.name = name;
    }

    public void updateGender(Gender gender) {
        if(gender==null) return;
        this.gender = gender;
    }

    public void updateAge(int age) {
        if((Integer)age==null) return;
        this.age = age;
    }

    public void updatePhoneNumber(String phoneNumber) {
        if(phoneNumber==null) return;
        this.phoneNumber = phoneNumber;
    }


    public void updateCompany(CompanyType company) {
        if(company==null) return;
        this.company = company;
    }

    public void updateDepartment(String department) {
        if(department==null) return;
        this.department = department;
    }

    public void updateKeywordName(KeywordName[] keywordName) {
        if(keywordName==null) return;
        if(this.keywordName != null) {
            this.keywordName.clear();
        }
        else this.keywordName = new ArrayList<>();

        this.keywordName.addAll(List.of(keywordName));
    }

    public void updateUserProfileFileUrl(String userProfilefileUrl){
        if(userProfilefileUrl==null) return;
        this.userProfilefileUrl = userProfilefileUrl;
    }

    public void updateRegion(List<Region> region){
        if(region == null || region.isEmpty()) return;
        this.region = region;
    }

    @Transactional
    public void update(UpdateMypageResponseDto updateMypageResponseDto) {
        this.updateName(updateMypageResponseDto.getName());
        this.updateAge(updateMypageResponseDto.getAge());
        this.updateGender(updateMypageResponseDto.getGender());
        this.updatePhoneNumber(updateMypageResponseDto.getPhoneNumber());
        this.updateCompany(updateMypageResponseDto.getCompany());
        this.updateDepartment(updateMypageResponseDto.getDepartment());
        this.updateKeywordName(updateMypageResponseDto.getKeywordName());
        this.updateUserProfileFileUrl(updateMypageResponseDto.getUserProfileFileUrl());
    }
}

