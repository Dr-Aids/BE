# syntax=docker/dockerfile:1.7

############################
# 1) Build stage (Gradle)
############################
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# Gradle 래퍼/설정만 먼저 복사해서 캐시 효과 극대화
# BuildKit 사용: 래퍼에 바로 실행권한 부여
COPY --chmod=0755 gradlew ./
COPY gradle ./gradle
COPY settings.gradle* build.gradle* gradle.properties* ./

# Gradle wrapper 준비(캐시 워밍업)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon help

# 나머지 소스 복사
COPY . .

# ⚠️ 윈도우에서 커밋된 경우 CRLF/실행권한 이슈 방지
RUN set -eux; \
    ls -l gradlew || true; \
    sed -i 's/\r$//' gradlew || true; \
    chmod 755 gradlew || true; \
    ls -l gradlew || true \

# 실제 빌드 (테스트 스킵은 필요 시 -x test 제거/변경)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon clean bootJar -x test

############################
# 2) Run stage (최소 이미지)
############################
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 산출물 복사 (명시적으로 app.jar로 이름 고정)
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Duser.timezone=Asia/Seoul"

# 실행
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
