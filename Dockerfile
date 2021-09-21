FROM openjdk:11-jdk as builder
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY build/libs/ci-board-0.0.1-SNAPSHOT.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11-jdk
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
ENV spring.profiles.active local
COPY --from=builder /usr/app/dependencies/ ./
COPY --from=builder /usr/app/spring-boot-loader/ ./
COPY --from=builder /usr/app/snapshot-dependencies/ ./
COPY --from=builder /usr/app/application/ ./
EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]