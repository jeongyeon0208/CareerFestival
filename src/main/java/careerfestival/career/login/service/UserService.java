package careerfestival.career.login.service;

import careerfestival.career.domain.User;
import careerfestival.career.domain.enums.Gender;
import careerfestival.career.domain.mapping.Region;
import careerfestival.career.login.dto.CustomUserDetails;
import careerfestival.career.login.dto.UserSignUpRequestDto;
import careerfestival.career.myPage.dto.MyPageUserInfoResponseDto;
import careerfestival.career.myPage.dto.UpdateMypageResponseDto;
import careerfestival.career.repository.RegionRepository;
import careerfestival.career.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public User signUp(UserSignUpRequestDto userSignUpRequestDto) {

        // DB에 존재하는지 여부 (email로 판단)
        boolean exists = userRepository.existsByEmail(userSignUpRequestDto.getEmail());
        if(exists){
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        //비밀번호 입력 확인
        if(!userSignUpRequestDto.getPassword().equals(userSignUpRequestDto.getCheckPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        User user = userSignUpRequestDto.toEntity();
        user.updatePassword(bCryptPasswordEncoder.encode(userSignUpRequestDto.getPassword()));
        user.setUserStatus();
        userRepository.save(user);

        return user;
    }


    @Transactional
    public void findUserByEmailAndUpdate(String email, UpdateMypageResponseDto updateMypageResponseDto){
        User findUser = userRepository.findByEmail(email);
        findUser.update(updateMypageResponseDto);

        Region region = regionRepository.findRegionByCityAndAddressLine(updateMypageResponseDto.getCity(), updateMypageResponseDto.getAddressLine());

        Gender gender = findUser.getGender();

        if(Gender.남성.equals(gender)){
            findUser.updateUserProfileFileUrl("classpath:Male_Profile.png");
        } else{
            findUser.updateUserProfileFileUrl("classpath:Female_Profile.png");
        }
    }

    @Transactional
    public User findUserByCustomUserDetails(CustomUserDetails customUserDetails){
        return userRepository.findByEmail(customUserDetails.getUsername());
    }

    @Transactional
    public MyPageUserInfoResponseDto fillMyPage(User user) {
        if(user.getRegion() != null){
            MyPageUserInfoResponseDto myPageUserInfoResponseDto = MyPageUserInfoResponseDto.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .phoneNumber(user.getPhoneNumber())
                    .company(user.getCompany())
                    .department(user.getDepartment())
                    .keywordNameList(user.getKeywordName())
                    .userProfilefileUrl(user.getUserProfilefileUrl())
                    .build();
            return myPageUserInfoResponseDto;
        } else {
            MyPageUserInfoResponseDto myPageUserInfoResponseDto = MyPageUserInfoResponseDto.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .userProfilefileUrl(user.getUserProfilefileUrl())
                    .build();
            return myPageUserInfoResponseDto;
        }
    }

}
