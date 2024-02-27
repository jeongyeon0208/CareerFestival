    package careerfestival.career.login.dto;

    import careerfestival.career.domain.User;
    import careerfestival.career.domain.enums.Role;
    import lombok.*;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public class UserSignUpRequestDto {

        private String email;
        private String password;
        private String checkPassword;
        private String name;
        private Role role;


        @Builder
        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .name(name)
                    .role(role)
                    .build();
        }
    }
