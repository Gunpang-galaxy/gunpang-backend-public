## Spring Batch 필요성

![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/e457359d-e60c-4d10-a3c7-0a01f5c78493)
매일 00시 모든 유저의 아바타에 대해  
운동, 식사, 수면에 대한 목표치 달성 여부에 따른 체력 감소 처리와  
일주일을 무사히 키운 아바타에 대해 레벨업 처리를  
순서 대로 중단 없이 처리하기 위해 사용


## MultiThread Processing

Spring Batch의 장점 중 하나인 병렬 처리를 통해  
각 chunk들을 MultiThread로 처리하여 배치 작업의 실행 속도 최적화

**Single Thread 사용 결과**
![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/6c943619-5e98-4c56-a625-cfefc1d8535d)

**Multi Thread 사용 결과**
![image](https://github.com/Gunpang-galaxy/gunpang-backend/assets/88764490/4efc9994-24a9-44b3-a1ee-31c1da074f3b)

  
542ms vs 1s576ms -> 약 190%의 성능 개선
