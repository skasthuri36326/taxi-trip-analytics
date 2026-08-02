# Taxi Trip Analytics

> Distributed batch analytics for NYC Yellow Taxi Trip data using Apache Spark (Spark SQL, DataFrames, and RDD) with equivalent Apache Pig implementations.

![Java](https://img.shields.io/badge/Java-17-blue)
![Apache Spark](https://img.shields.io/badge/Apache%20Spark-3.5-orange)
![Hadoop](https://img.shields.io/badge/Hadoop-HDFS%20%7C%20YARN-yellowgreen)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green)

---

## Overview

Taxi Trip Analytics is a distributed batch analytics project for processing NYC Yellow Taxi Trip data using Apache Spark and Apache Pig.

The project implements identical analytical workloads using Spark SQL, Spark DataFrames, Spark RDD, and Apache Pig, enabling comparison across multiple processing frameworks while maintaining a consistent application architecture.

---

## Features

- Apache Spark SQL / DataFrame implementation
- Apache Spark RDD implementation
- Apache Pig implementation
- Local Spark execution
- Hadoop (HDFS & YARN) execution
- Docker configuration
- Benchmark utilities

---

## Analytics Jobs

| Job | Description |
|------|-------------|
| Single Record Lookup | Retrieve a taxi trip using multiple business-key filters |
| Rate Code Filter | Filter records by `RatecodeID` |
| Payment Type Aggregation | Group records by payment type and calculate counts |

---

## Technology Stack

- Java 17
- Apache Spark
- Spark SQL / DataFrames
- Spark RDD
- Apache Pig
- Hadoop (HDFS & YARN)
- Maven
- Docker
- JUnit 5

---

## Project Structure

```text
taxi-trip-analytics/
├── src/
├── data/
├── docker/
├── docs/
├── pig/
├── scripts/
├── benchmarks/
├── pom.xml
├── Dockerfile
└── README.md
```

### Java Package Structure

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

---

## Dataset

This project uses the publicly available **NYC Taxi & Limousine Commission (TLC)** Yellow Taxi Trip dataset.

- Sample dataset: `data/sample/`
- Full dataset: https://www.nyc.gov/site/tlc/about/tlc-trip-record-data.page

The sample dataset is intended for local development and testing. Replace it with the complete TLC dataset for large-scale execution.

---

## Build

```bash
mvn clean package
```

---

## Local Execution

### Spark SQL

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master local[*] \
  target/taxi-trip-analytics.jar \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment
```

### Spark RDD

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master local[*] \
  target/taxi-trip-analytics.jar \
  --engine rdd \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment
```

---

## Docker Execution

Run the project locally using Docker.

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

---

## Hadoop Cluster Execution (HDFS/YARN)

### Upload the dataset

```bash
hdfs dfs -mkdir -p /user/data/taxi

hdfs dfs -put yellow_tripdata_*.csv /user/data/taxi/
```

---

### Single Record Lookup

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.SingleRecordLookupJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_single_row_lookup_output
```

Verify:

```bash
hadoop fs -ls /user/output/spark_single_row_lookup_output

hadoop fs -cat /user/output/spark_single_row_lookup_output/part*

hadoop fs -cat /user/output/spark_single_row_lookup_output/part* | wc -l
```

---

### Rate Code Filter

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.RateCodeFilterJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_filtered_output
```

Verify:

```bash
hadoop fs -ls /user/output/spark_filtered_output

hadoop fs -cat /user/output/spark_filtered_output/part*

hadoop fs -cat /user/output/spark_filtered_output/part* | wc -l
```

---

### Payment Type Aggregation

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.PaymentTypeAnalyticsJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_grouped_output
```

Verify:

```bash
hadoop fs -ls /user/output/spark_grouped_output

hadoop fs -cat /user/output/spark_grouped_output/part*
```

> **Note**
>
> Spark writes output as one or more `part-*` files. Use the commands above to inspect the generated results directly from HDFS.

---

## Apache Pig

Equivalent Apache Pig implementations are provided for each analytical workload.

Available scripts:

- `single-record-lookup.pig`
- `ratecode-filter.pig`
- `payment-type-analytics.pig`

Execute a script:

```bash
pig payment-type-analytics.pig
```

The Pig scripts read input data from HDFS and write results back to HDFS.

---

## Performance Evaluation

The repository includes benchmark utilities for comparing Spark SQL, Spark RDD, and Apache Pig using identical analytical workloads and input datasets.

Additional benchmark notes are available in:

```text
docs/Performance-Comparison.md
```

---

## Roadmap

- Parameterized job configuration
- Parameterized Apache Pig scripts
- Additional Spark SQL analytical workloads
- Apache Spark standalone deployment
- Amazon EMR deployment
- Automated benchmark reporting

---

## License

This project is licensed under the MIT License.