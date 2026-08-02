FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
COPY data ./data
RUN mvn -q -DskipTests package

FROM bitnami/spark:3.5.1
WORKDIR /app
COPY --from=build /workspace/target/taxi-trip-analytics-1.0.0.jar /app/taxi-trip-analytics.jar
COPY data /app/data
COPY docs /app/docs
ENTRYPOINT ["/opt/bitnami/spark/bin/spark-submit", "--class", "com.proapps.taxianalytics.cli.Application", "--master", "local[*]", "/app/taxi-trip-analytics.jar"]
