## career 내부 디렉토리 구조
```
C:.
│  CareerApplication.java
│  
├─apiPayload
│  │  ApiResponse.java
│  │  
│  ├─code
│  │  │  BaseCode.java
│  │  │  BaseErrorCode.java
│  │  │  ErrorReasonDTO.java
│  │  │  ReasonDTO.java
│  │  │  
│  │  └─status
│  │          ErrorStatus.java
│  │          SuccessStatus.java
│  │          
│  └─exception
│      │  ExceptionAdvice.java
│      │  GeneralException.java
│      │  
│      └─handler
│              TempHandler.java
│              
├─comments
│  ├─Controller
│  │      CommentsController.java
│  │      
│  ├─dto
│  │      CommentRequestDto.java
│  │      CommentResponseDto.java
│  │      
│  └─Service
│          CommentService.java
│          
├─config
│      SecurityConfig.java
│      
├─domain
│  │  Event.java
│  │  Record.java
│  │  User.java
│  │  
│  ├─common
│  │      BaseEntity.java
│  │      
│  ├─enums
│  │      Category.java
│  │      Gender.java
│  │      KeywordName.java
│  │      Role.java
│  │      UserStatus.java
│  │      
│  └─mapping
│          Comment.java
│          EventImage.java
│          Follow.java
│          Participate.java
│          Region.java
│          Wish.java
│          
├─dto
│      CustomUserDetails.java
│      
├─exception
│      CustomException.java
│      ErrorCode.java
│      
├─jwt
│      JWTFilter.java
│      JWTUtil.java
│      LoginFilter.java
│      
├─login
│  ├─api
│  │      UserController.java
│  │      
│  ├─dto
│  │      UpdateUserDetailRequestDto.java
│  │      UserSignInRequestDto.java
│  │      UserSignRoleRequestDto.java
│  │      UserSignUpRequestDto.java
│  │      
│  └─service
│          CustomUserDetailsService.java
│          UserService.java
│          
├─participate
│  ├─controller
│  │      ParticipateController.java
│  │      
│  ├─dto
│  │      ParticipateRequestDto.java
│  │      ParticipateResponseDto.java
│  │      
│  └─service
│          ParticipateService.java
│          
├─record
│  ├─controller
│  │      RecordController.java
│  │      
│  ├─dto
│  │      RecordEtcDto.java
│  │      RecordLectureSeminarDto.java
│  │      RecordMainResponseDto.java
│  │      
│  └─service
│          RecordService.java
│          
├─register
│  ├─controller
│  │      RegisterController.java
│  │      
│  ├─dto
│  │      RegisterEventDto.java
│  │      
│  └─service
│          RegisterService.java
│          
└─repository
        CommentRepository.java
        EventImageRepository.java
        EventRepository.java
        ParticipateRepository.java
        RecordRepository.java
        UserRepository.java
```
