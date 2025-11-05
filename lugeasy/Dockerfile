# syntax=docker/dockerfile:1

# 런타임 전용(더 가벼움). 빌드가 이미 완료되어 build/libs/*.jar가 존재한다고 가정
FROM eclipse-temurin:21-jre

# 기본 ENV (런타임에 덮어쓰기 가능)
ENV SPRING_PROFILE=dev
ENV JAVA_OPTS=""

# 작업 디렉터리
WORKDIR /app

# 빌드 산출물 JAR 복사 (컨텍스트 기준 경로)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar

# 필요 시 오픈 포트 (스프링 기본 8080)
EXPOSE 8080

# JVM 옵션은 -jar 앞에 와야 함
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILE} -jar /app/app.jar"]
