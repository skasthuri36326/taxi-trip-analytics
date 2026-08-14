FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package


FROM apache/spark:3.5.1-java17

WORKDIR /app

COPY --from=build /workspace/target/taxi-trip-analytics-1.0.0.jar /app/taxi-trip-analytics.jar
COPY data /app/data

ENTRYPOINT ["/opt/spark/bin/spark-submit", \
            "--master", "local[*]", \
            "--class", "com.proapps.taxianalytics.cli.Application", \
            "/app/taxi-trip-analytics.jar"]