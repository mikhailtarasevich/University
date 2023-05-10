FROM openjdk:11-jre-slim as builder
WORKDIR app
ADD target/university-ui-0.0.1-SNAPSHOT.jar ./
RUN java -Djarmode=layertools -jar university-ui-0.0.1-SNAPSHOT.jar extract

FROM openjdk:11-jre-slim
WORKDIR app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]