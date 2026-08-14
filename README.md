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

The project implements equivalent analytical workloads using:

- Apache Spark SQL
- Apache Spark DataFrames
- Apache Spark RDD
- Apache Pig

The implementations use a consistent application architecture and can be executed locally, using Docker, or on a Hadoop cluster using HDFS and YARN.

---

## Features

- Apache Spark SQL / DataFrame implementation
- Apache Spark RDD implementation
- Apache Pig implementation
- Local Spark execution
- Hadoop HDFS and YARN execution
- Docker configuration
- Benchmark utilities
- Equivalent SQL and RDD analytical workloads
- JUnit 5 tests
- Maven build configuration

---

## Analytics Jobs

| Job | Description |
|---|---|
| Single Record Lookup | Retrieve a taxi trip using multiple business-key filters |
| Rate Code Filter | Filter records by `RatecodeID` |
| Payment Type Aggregation | Group records by payment type and calculate counts |

---

## Technology Stack

- Java 17
- Apache Spark 3.5.1
- Spark SQL / DataFrames
- Spark RDD
- Apache Pig
- Hadoop HDFS
- Hadoop YARN
- Maven
- Docker
- JUnit 5

---

## Project Structure

```text
taxi-trip-analytics/
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
├── data/
│   └── sample/
├── docker/
├── docs/
├── pig/
├── scripts/
├── benchmarks/
├── output/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
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

# Dataset

This project uses the publicly available NYC Taxi & Limousine Commission (TLC) Yellow Taxi Trip dataset.

Official dataset:

https://www.nyc.gov/site/tlc/about/tlc-trip-record-data.page

The sample dataset is stored under:

```text
data/sample/
```

The sample dataset is intended for local development and testing.

For large-scale execution, download and use the complete TLC Yellow Taxi Trip dataset.

---

## Dataset Setup

Create the local dataset directory if it does not already exist:

```bash
mkdir -p data/sample
```

Place the sample Yellow Taxi Trip CSV file at:

```text
data/sample/yellow_tripdata_sample.csv
```

For Hadoop execution, the complete dataset can be uploaded to HDFS.

Create the HDFS directory:

```bash
hdfs dfs -mkdir -p /user/data/taxi
```

Upload Yellow Taxi Trip CSV files:

```bash
hdfs dfs -put yellow_tripdata_*.csv /user/data/taxi/
```

Verify that the files were uploaded:

```bash
hdfs dfs -ls /user/data/taxi
```

---

# Build

Build the project using Maven:

```bash
mvn clean package
```

The packaged JAR is generated under:

```text
target/taxi-trip-analytics-1.0.0.jar
```

Depending on the Maven configuration, the runnable JAR may also be available as:

```text
target/taxi-trip-analytics.jar
```

Run the tests separately with:

```bash
mvn test
```

---

# Local Execution

## Spark SQL / DataFrame

Run Payment Type Aggregation using Spark SQL:

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

The SQL implementation writes the aggregation result as CSV.

Example:

```text
payment_type,count
1,2
```

---

## Spark RDD

Run the equivalent Payment Type Aggregation using Spark RDD:

```bash
spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine rdd \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-rdd
```

The RDD implementation writes the data rows without a CSV header.

Example:

```text
1,2
```

---

# Using a Local Spark Installation

If Spark is installed under:

```text
~/opt/spark-3.5.1-bin-hadoop3
```

run the SQL implementation with:

```bash
~/opt/spark-3.5.1-bin-hadoop3/bin/spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

Run the RDD implementation with:

```bash
~/opt/spark-3.5.1-bin-hadoop3/bin/spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine rdd \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-rdd
```

The quotes around `'local[*]'` prevent Zsh from interpreting `[*]` as a filename pattern.

---

# Compare SQL and RDD Results

The SQL and RDD implementations should produce the same analytical values.

SQL:

```text
payment_type,count
1,2
```

RDD:

```text
1,2
```

The SQL implementation writes a CSV header while the RDD implementation writes only the data rows.

Therefore, a direct `diff` will show a difference even when the calculated values are identical.

Compare only the data rows:

```bash
diff \
  <(tail -n +2 output/payment-sql/part-*) \
  <(cat output/payment-rdd/part-*)
```

If there is no output from `diff`, the SQL and RDD analytical results are identical.

---

# Inspect Spark Output

Spark output directories normally contain files such as:

```text
_SUCCESS
part-00000-*.csv
*.crc
```

The actual analytical results are stored in the `part-*` files.

Inspect the SQL result:

```bash
cat output/payment-sql/part-*
```

Inspect the RDD result:

```bash
cat output/payment-rdd/part-*
```

---

# Docker Execution

The project can also be executed locally using Docker.

## Docker Compose

Build the Docker image and start the application:

```bash
docker compose up --build
```

The Docker configuration uses:

- Maven with Java 17 for building
- Apache Spark 3.5.1 with Java 17 for execution
- The project JAR
- The sample dataset

Spark runs in local mode inside the container.

---

## Build Docker Image Directly

```bash
docker build -t taxi-trip-analytics .
```

---

## Run Spark SQL Using Docker

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine sql \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-sql
```

---

## Run Spark RDD Using Docker

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine rdd \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-rdd
```

---

## Docker Output

When running with Docker Compose, output is written under:

```text
/app/output/
```

For example:

```text
/app/output/payment-sql/
/app/output/payment-rdd/
```

Spark normally creates:

```text
_SUCCESS
part-00000-*.csv
```

Associated checksum files may also be generated by the underlying filesystem.

---

# Hadoop Cluster Execution

The application can be executed against a Hadoop cluster using HDFS and YARN.

## Upload Dataset to HDFS

Create the HDFS directory:

```bash
hdfs dfs -mkdir -p /user/data/taxi
```

Upload the taxi dataset:

```bash
hdfs dfs -put yellow_tripdata_*.csv /user/data/taxi/
```

Verify the files:

```bash
hdfs dfs -ls /user/data/taxi
```

---

# Single Record Lookup

Run the Single Record Lookup Spark job:

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.SingleRecordLookupJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_single_row_lookup_output
```

Verify the output:

```bash
hadoop fs -ls /user/output/spark_single_row_lookup_output
```

Display the result:

```bash
hadoop fs -cat /user/output/spark_single_row_lookup_output/part*
```

Count the output records:

```bash
hadoop fs -cat /user/output/spark_single_row_lookup_output/part* | wc -l
```

---

# Rate Code Filter

Run the Rate Code Filter Spark job:

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.RateCodeFilterJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_filtered_output
```

Verify the output:

```bash
hadoop fs -ls /user/output/spark_filtered_output
```

Display the result:

```bash
hadoop fs -cat /user/output/spark_filtered_output/part*
```

Count the output records:

```bash
hadoop fs -cat /user/output/spark_filtered_output/part* | wc -l
```

---

# Payment Type Aggregation

Run the Payment Type Aggregation Spark job:

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.jobs.PaymentTypeAnalyticsJob \
  target/taxi-trip-analytics.jar \
  /user/data/taxi \
  /user/output/spark_grouped_output
```

Verify the output:

```bash
hadoop fs -ls /user/output/spark_grouped_output
```

Display the result:

```bash
hadoop fs -cat /user/output/spark_grouped_output/part*
```

Spark writes output as one or more `part-*` files.

---

# Apache Pig

Equivalent Apache Pig implementations are provided for the analytical workloads.

Available scripts:

```text
pig/
├── single-record-lookup.pig
├── ratecode-filter.pig
└── payment-type-analytics.pig
```

The Pig scripts read input data from HDFS and write the analytical results back to HDFS.

## Run Payment Type Analytics with Pig

```bash
pig pig/payment-type-analytics.pig
```

The script performs the equivalent payment type aggregation using Apache Pig.

---

# SQL and RDD Equivalence

The Spark SQL and Spark RDD implementations are designed to perform equivalent analytical operations.

For the Payment Type Aggregation workload, both implementations:

1. Read the Yellow Taxi Trip CSV data.
2. Extract the payment type.
3. Group records by payment type.
4. Count the number of records for each payment type.
5. Write the resulting aggregation.

The SQL implementation uses Spark SQL/DataFrame operations.

The RDD implementation uses Spark RDD transformations and actions.

The expected analytical values are therefore equivalent even though the output formatting differs.

SQL:

```text
payment_type,count
1,2
```

RDD:

```text
1,2
```

The difference is the CSV header generated by the SQL/DataFrame writer, not the analytical result.

---

# Performance Evaluation

The repository includes benchmark utilities for comparing:

- Spark SQL
- Spark RDD
- Apache Pig

The workloads use equivalent analytical operations and the same input dataset so processing performance can be compared.

Performance measurements can include:

- Execution time
- Input size
- Output size
- Processing engine
- Analytical workload

Additional benchmark information is available in:

```text
docs/Performance-Comparison.md
```

---

# Output Format

Spark writes results using its distributed output format.

Typical SQL output:

```text
output/payment-sql/
├── _SUCCESS
└── part-00000-*.csv
```

Typical RDD output:

```text
output/payment-rdd/
├── _SUCCESS
└── part-00000-*
```

Additional `.crc` files may be generated depending on the filesystem and environment.

The important files for inspecting analytical results are the `part-*` files.

---

# Testing

Run the complete Maven test suite:

```bash
mvn test
```

The project uses JUnit 5 for unit testing.

For example, the CSV parser test verifies that a valid CSV line can be parsed into the expected number of fields.

---

# Application Usage

The application accepts the following main parameters:

```text
--engine
--job
--input
--output
```

Example SQL configuration:

```bash
--engine sql
--job payment
--input data/sample/yellow_tripdata_sample.csv
--output output/payment-sql
```

Example RDD configuration:

```bash
--engine rdd
--job payment
--input data/sample/yellow_tripdata_sample.csv
--output output/payment-rdd
```

---

# Reproducible Local Workflow

The complete local workflow can be performed using the following commands.

## 1. Build

```bash
mvn clean package
```

## 2. Run SQL

```bash
~/opt/spark-3.5.1-bin-hadoop3/bin/spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

## 3. Run RDD

```bash
~/opt/spark-3.5.1-bin-hadoop3/bin/spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics.jar \
  --engine rdd \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-rdd
```

## 4. Inspect SQL Result

```bash
cat output/payment-sql/part-*
```

## 5. Inspect RDD Result

```bash
cat output/payment-rdd/part-*
```

## 6. Compare Analytical Results

```bash
diff \
  <(tail -n +2 output/payment-sql/part-*) \
  <(cat output/payment-rdd/part-*)
```

No output from `diff` indicates that the SQL and RDD data values match.

---

# Docker Workflow

## 1. Build and Start

```bash
docker compose up --build
```

## 2. Run SQL

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine sql \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-sql
```

## 3. Run RDD

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine rdd \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-rdd
```

---

# Roadmap

- Parameterized job configuration
- Parameterized Apache Pig scripts
- Additional Spark SQL analytical workloads
- Apache Spark standalone deployment
- Amazon EMR deployment
- Automated benchmark reporting
- Extended integration testing

---

# License

This project is licensed under the MIT License.