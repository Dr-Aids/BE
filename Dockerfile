# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# 1) 래퍼/세팅만 먼저 복사
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle* build.gradle* gradle.properties* ./

# (윈도우 커밋 대비) CRLF 제거 + 권한
RUN sed -i 's/\r$//' gradlew || true && chmod 755 gradlew || true

# 캐시 워밍업
RUN --mount=type=cache,target=/root/.gradle sh ./gradlew --no-daemon help

# 2) 나머지 소스
COPY . .

# ⚠️ COPY . .로 gradlew가 덮여써질 수 있으니 다시 복구
RUN sed -i 's/\r$//' gradlew || true && chmod 755 gradlew || true && ls -l gradlew

# 실제 빌드 (테스트 필요 없으면 -x test 유지)
RUN --mount=type=cache,target=/root/.gradle sh ./gradlew --no-daemon clean bootJar -x test

# ---- runtime ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Duser.timezone=Asia/Seoul"
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
