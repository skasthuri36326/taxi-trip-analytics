# Taxi Trip Analytics

> Distributed batch analytics application for NYC Yellow Taxi trip data using Apache Spark (Spark SQL, DataFrames, and RDD) with Apache Pig reference jobs.

![Java](https://img.shields.io/badge/Java-17-blue)
![Apache Spark](https://img.shields.io/badge/Apache%20Spark-3.5-orange)
![Maven](https://img.shields.io/badge/Maven-3.x-red)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![Apache Pig](https://img.shields.io/badge/Apache-Pig-yellow)
![License](https://img.shields.io/badge/License-MIT-green)

---

## Overview

Taxi Trip Analytics demonstrates the same batch workloads across Apache Spark SQL / DataFrames, Apache Spark RDD, and Apache Pig. The project is organized with a shared command-line interface, modular job implementations, Docker support, and benchmarking utilities.

## Features

- Spark SQL / DataFrame jobs
- Spark RDD jobs
- Apache Pig reference scripts
- Local Spark execution
- Hadoop (HDFS/YARN) execution
- Docker support
- Benchmark utilities

## Analytics jobs

| Job | Description |
|------|-------------|
| Single Record Lookup | Retrieve a taxi trip using multiple business-key filters |
| Rate Code Filter | Filter records by `RatecodeID` |
| Payment Type Analytics | Group records by payment type and calculate counts |

## Technology stack

- Java 17
- Apache Spark 3.5.x
- Spark SQL / DataFrames
- Spark RDD
- Apache Pig
- Hadoop (HDFS & YARN)
- Maven
- Docker
- JUnit 5

## Project structure

```text
src/
data/
pig/
docker/
docs/
scripts/
benchmarks/
pom.xml
README.md
```

Java package:

```text
com.proapps.taxianalytics
├── cli
├── config
├── jobs
│   ├── sql
│   └── rdd
├── model
├── parser
├── benchmark
├── util
└── exception
```

## Dataset

This project uses the public NYC Taxi & Limousine Commission (TLC) Yellow Taxi dataset.

- Sample data: `data/sample/`
- Full dataset: https://www.nyc.gov/site/tlc/about/tlc-trip-record-data.page

## Build

```bash
mvn clean package
```

## Run locally

Spark SQL:

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master local[*] \
  target/taxi-trip-analytics-1.0.0.jar \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment
```

Spark RDD:

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master local[*] \
  target/taxi-trip-analytics-1.0.0.jar \
  --engine rdd \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment
```

## Docker

```bash
docker compose up --build
```

Or build and run directly:

```bash
docker build -t taxi-trip-analytics .
docker run --rm taxi-trip-analytics \
  --engine sql \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment
```

## Hadoop (HDFS/YARN)

```bash
hdfs dfs -put yellow_tripdata_*.csv /user/data/taxi/

spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.cli.Application \
  target/taxi-trip-analytics-1.0.0.jar \
  --engine sql \
  --job payment \
  --input /user/data/taxi \
  --output /user/output/payment
```

## Apache Pig

Reference Pig scripts are included for the same workloads:

- `single-record-lookup.pig`
- `ratecode-filter.pig`
- `payment-type-analytics.pig`

Example:

```bash
pig payment-type-analytics.pig
```

## Benchmarking

Benchmark notes and comparison details are available in `docs/Performance-Comparison.md`.

## Roadmap

- Parameterized job configuration
- Additional Spark SQL workloads
- Automated benchmark reporting
- Apache Spark standalone deployment
- Amazon EMR deployment
- Parameterized Apache Pig scripts

## License

This project is licensed under the MIT License.
