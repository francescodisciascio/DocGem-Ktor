FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon

FROM openjdk:11
EXPOSE 8066:8066
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/docgem-ktor.jar
ENTRYPOINT ["java","-jar","/app/docgem-ktor.jar"]