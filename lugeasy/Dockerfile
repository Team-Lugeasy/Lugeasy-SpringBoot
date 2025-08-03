#FROM eclipse-temurin:21-jdk
FROM openjdk:21-jdk

# 환경변수 기본값
ENV SPRING_PROFILE=dev

# JAR 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 컨테이너 실행 시 profile을 런타임에 결정 가능
ENTRYPOINT ["sh", "-c", "java -jar -Dspring.profiles.active=$SPRING_PROFILE /app.jar"]
