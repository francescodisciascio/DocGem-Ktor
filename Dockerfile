FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM amazoncorretto:11-alpine
EXPOSE 8097
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/DocGem-Ktor-0.0.1-all.jar /app/docgem-ktor.jar
ENTRYPOINT ["java","-jar","/app/docgem-ktor.jar"]