# syntax=docker/dockerfile:1.7

############################
# 1) Build stage (Gradle)
############################
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# Gradle 래퍼/설정만 먼저 복사해서 캐시 효과 극대화
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle* build.gradle* gradle.properties* ./
RUN chmod +x gradlew

# Gradle wrapper 다운로드 및 기본 준비(의존성 일부 캐시)
# BuildKit 캐시를 사용해 이후 빌드 속도 향상
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon help

# 나머지 소스 복사 후 실제 빌드
COPY . .
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon clean bootJar

############################
# 2) Run stage (최소 이미지)
############################
FROM eclipse-temurin:17-jre
WORKDIR /app

# (헬스체크에 curl을 쓰고 싶다면 주석 해제)
# RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 빌드 산출물 복사 (부트 JAR만 1개라고 가정하기 어려우니, 와일드카드로 디렉터리에 복사)
COPY --from=builder /workspace/build/libs/*.jar /app/

EXPOSE 8080
ENV JAVA_OPTS=""
# /app/*.jar 패턴은 1개일 때 정상 실행됨
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/*.jar"]
