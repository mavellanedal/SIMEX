FROM eclipse-temurin:11-jdk-jammy AS build

WORKDIR /src

RUN apt-get update \
    && apt-get install -y --no-install-recommends kotlin \
    && rm -rf /var/lib/apt/lists/*

COPY ["FileServer/src/Main.kt", "src/Main.kt"]

RUN mkdir -p /build
RUN kotlinc src/Main.kt -include-runtime -d /build/file-server.jar

FROM eclipse-temurin:11-jre-jammy AS final

WORKDIR /app

RUN mkdir -p /app/arxius

COPY --from=build /build/file-server.jar /app/file-server.jar

EXPOSE 5000
VOLUME ["/app/arxius"]

ENTRYPOINT ["java", "-jar", "/app/file-server.jar"]
