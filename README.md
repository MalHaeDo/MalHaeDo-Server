# ☀️ 말해도 ☀️
![img_1.png](img_1.png)

## 📃 서비스 소개 📃
"힘들고 지칠 때, 어디다 털어놓고 싶지만 이야기하기 어려워요." <br>
"나만 아는, 혼자 있는 곳에서 털어놓을 수 있는 장소가 필요해요." <br>
부담없이 털어놓고 조언을 들을 수 있는 AI 서비스 '말해도'에 감정을 흘려보내세요!
<br/>
<br/>

## 😽 핵심 기능 😽
- 부정적인 감정을 유리병에 담아 섬으로 보내요
- 유리병을 보고 주민들이 따뜻한 공감이 담긴 답문을 보내요
- 편지의 내용을 토대로 이장님이 담백한 위로와 함께 어울리는 노래를 추천해줘요
  <br/>
  <br/>

## 🛠 Tech Stack 🛠
### Backend
- ![Java](https://img.shields.io/badge/Java-ED8B00?style=plastic&logo=&logoColor=white)
- ![Clova Studio](https://img.shields.io/badge/Clova%20Studio%20API-7FE719?style=plastic&logo=&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=plastic&logo=Spring&logoColor=white)
- ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=plastic&logo=Spring%20Security&logoColor=white)
- ![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=plastic&logo=Spring&logoColor=white)
- ![MYSQL](https://img.shields.io/badge/MYSQL-4169E1?style=plastic&logo=MYSQL&logoColor=white)

### Infrastructure
- ![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=plastic&logo=Amazon%20EC2&logoColor=white)
- ![Amazon RDS](https://img.shields.io/badge/Amazon_RDS-527FFF?style=plastic&logo=Amazon%20RDS&logoColor=white)


### DevOps
- ![Github Actions](https://img.shields.io/badge/Github_Actions-2088FF?style=plastic&logo=Github%20Actions&logoColor=white)

### Security
- ![JSON Web Tokens](https://img.shields.io/badge/JSON_Web_Tokens-000000?style=plastic&logo=JSON%20Web%20Tokens&logoColor=white)
  <br/>
  <br/>


## 🌱 ERD 🌱
![img.png](img.png)
<br/>
<br/>

## 📙 API Docs 📙
[Swagger 문서 확인하러 가기](http://malhaedo-server.store/swagger-ui/index.html)
<br/>
<br/>

## 폴더 구조
```markdown
└─com
└─backend
└─malhaedo
│  MalhaedoApplication.java
│
├─domain
│  ├─letter
│  │  ├─controller
│  │  │      LetterController.java
│  │  │
│  │  ├─converter
│  │  │      LetterConverter.java
│  │  │
│  │  ├─dto
│  │  │      LetterRequestDTO.java
│  │  │      LetterResponse.java
│  │  │
│  │  ├─entity
│  │  │      Letter.java
│  │  │
│  │  ├─repository
│  │  │      LetterRepository.java
│  │  │
│  │  └─service
│  │          LetterService.java
│  │          LetterServiceImpl.java
│  │
│  ├─member
│  │  ├─controller
│  │  │      MemberController.java
│  │  │
│  │  ├─converter
│  │  │      MemberConverter.java
│  │  │
│  │  ├─dto
│  │  │      MemberRequestDTO.java
│  │  │      MemberResponseDTO.java
│  │  │
│  │  ├─entity
│  │  │      Member.java
│  │  │
│  │  ├─repository
│  │  │      MemberRepository.java
│  │  │
│  │  └─service
│  │          MemberService.java
│  │          MemberServiceImpl.java
│  │
│  ├─recommend
│  │  ├─controller
│  │  │      RecommendController.java
│  │  │
│  │  ├─converter
│  │  │      RecommendConverter.java
│  │  │
│  │  ├─dto
│  │  │      RecommendResponseDTO.java
│  │  │
│  │  ├─entity
│  │  │      Song.java
│  │  │
│  │  ├─repository
│  │  │      RecommendRepository.java
│  │  │
│  │  └─service
│  │          RecommendService.java
│  │          RecommendServiceImpl.java
│  │
│  └─reply
│      ├─controller
│      │      ReplyController.java
│      │
│      ├─converter
│      │      ReplyConverter.java
│      │
│      ├─dto
│      │      ReplyResponseDTO.java
│      │
│      ├─entity
│      │      Reply.java
│      │
│      ├─repository
│      │      ReplyRepository.java
│      │
│      └─service
│              ReplyService.java
│              ReplyServiceImpl.java
│
└─global
├─annotation
│      AuthenticatedMemberResolver.java
│      CurrentMember.java
│
├─common
│  │  BaseEntity.java
│  │
│  └─enums
│          Resident.java
│
├─config
│      CorsConfig.java
│      SecurityConfig.java
│      SwaggerConfig.java
│      WebClientConfig.java
│      WebConfig.java
│
├─error
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
├─jwt
│  ├─filter
│  │      JwtFilter.java
│  │
│  ├─handler
│  │      JwtAccessDeniedHandler.java
│  │      JwtAuthenticationEntryPoint.java
│  │
│  └─principal
│          PrincipalDetails.java
│
├─oauth
│  ├─handler
│  │      OAuthLoginFailureHandler.java
│  │      OAuthLoginSuccessHandler.java
│  │
│  ├─service
│  │      PrincipalDetailsService.java
│  │      PrincipalOauth2UserService.java
│  │
│  └─userinfo
│          KakaoUserInfo.java
│          OAuth2UserInfo.java
│
├─prompt
│  └─dto
│          ClovaReply.java
│          ClovaResponse.java
│          ClovaSong.java
│          SummaryReply.java
│          SummaryResponse.java
│
├─token
│  ├─controller
│  │      TokenController.java
│  │
│  ├─converter
│  │      TokenConverter.java
│  │
│  ├─dto
│  │      TokenResponseDTO.java
│  │
│  ├─entity
│  │      RefreshToken.java
│  │
│  ├─repository
│  │      RefreshTokenRepository.java
│  │
│  └─service
│          TokenService.java
│          TokenServiceImpl.java
│
└─util
        CookieUtil.java
        JwtUtil.java
```
<br/>
<br/>