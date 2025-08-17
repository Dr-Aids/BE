# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace

# 1) Gradle 래퍼/세팅만 먼저 복사 (캐시 최적화)
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle* build.gradle* gradle.properties* ./

# (윈도우 CRLF 대비) 권한/개행 정리
RUN sed -i 's/\r$//' gradlew || true && chmod 755 gradlew || true

# Gradle 캐시 워밍업 (소스 없음)
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon help

# 2) 나머지 소스
COPY . .

# COPY로 gradlew 덮였을 수 있으니 다시 정리
RUN sed -i 's/\r$//' gradlew || true && chmod 755 gradlew || true && ls -l gradlew

# 실제 빌드 (테스트 안 돌릴거면 -x test 유지)
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon clean bootJar -x test

# === 핵심: 산출 JAR를 고정 이름으로 표준화 (plain.jar 제외) ===
RUN JAR=$(ls build/libs/*.jar | grep -v '\-plain\.jar' | head -n 1) \
    && echo "Using JAR: $JAR" \
    && cp "$JAR" /workspace/app.jar

# ---- runtime ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# 표준화된 JAR만 복사
COPY --from=builder /workspace/app.jar /app/app.jar

# 내부 포트
EXPOSE 8080

# 시간대/메모리 옵션
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -Duser.timezone=Asia/Seoul"

# 프로필/DB 등은 런타임에 --env-file 로 주입됨 (.env 읽음)
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
