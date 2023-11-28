## Spring Batch 필요성

![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/e457359d-e60c-4d10-a3c7-0a01f5c78493)
매일 00시 모든 유저의 아바타에 대해  
운동, 식사, 수면에 대한 목표치 달성 여부에 따른 체력 감소 처리와  
일주일을 무사히 키운 아바타에 대해 레벨업 처리를  
순서 대로 중단 없이 처리하기 위해 사용

<br>
    
## MultiThread Processing

Spring Batch의 장점 중 하나인 병렬 처리를 통해  
각 chunk들을 MultiThread로 처리하여 배치 작업의 실행 속도 최적화

**Single Thread 사용 결과**
![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/6c943619-5e98-4c56-a625-cfefc1d8535d)

**Multi Thread 사용 결과**
![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/4efc9994-24a9-44b3-a1ee-31c1da074f3b)

  
542ms vs 1s576ms -> 약 190%의 성능 개선

<br>
    
## 알림 기능

![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/55910792/2c812375-f6e6-4553-8b41-c6ca303ae0c2)
매일 5 ~ 10시에 기록된 식사는 아침,  
11 ~ 16시에 기록된 식사는 점심,  
17 ~ 21시에 기록된 식사는 저녁으로 분류하고  
나머지 시간에는 식사 기록이 되지 않게 구현  
이때, 식사하는 것을 사용자가 잊지 않도록 FCM을 이용하여 9, 15, 20시에 식사 알림을 전송

<br>
    
## 인증/인가

- 인증
  - 로그인 시에 JWT Token 발급
  - Access Token은 Client에 반환, Refresh Token은 Redis에 저장
- 인가
  - JWT Interceptor로 인가가 필요한 요청의 Header로 전달된 JWT Token의 유효성 검사
  - `@NoAuth` 어노테이션 구현하여 인가가 필요하지 않은 요청을 구분
  - 만료된 Token에 대해서 Exception을 반환하여 Client에서 Token 재발급을 요청하면, Refresh Token을 이용해 Access Token을 재발급하여 반환

<br>
    
