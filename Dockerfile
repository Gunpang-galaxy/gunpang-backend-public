FROM gradle:8.3.0-jdk17 as builder
WORKDIR /build

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드
#COPY ./build.gradle /build/
#COPY ./settings.gradle /build/
COPY /backend/gunpang/build.gradle /build/
COPY /backend/gunpang/settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 빌더 이미지에서 애플리케이션 빌드
#COPY ./ /build
COPY /backend/gunpang/ /build
RUN gradle build -x test --parallel

FROM openjdk:17-ea-11-jdk-slim
WORKDIR /app

# 빌더 이미지에서 jar 파일만 복사
COPY --from=builder /build/build/libs/gunpang-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

USER nobody
ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "gunpang-0.0.1-SNAPSHOT.jar"              \
]