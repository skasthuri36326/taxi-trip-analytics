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

- Apache Spark SQL / DataFrames
- Apache Spark RDD
- Apache Pig

The application can run locally, with Docker, or on a Hadoop cluster using HDFS and YARN.

The Spark SQL and RDD implementations perform equivalent analytical operations, allowing their results and execution performance to be compared.

---

## Features

- Apache Spark SQL / DataFrame implementation
- Apache Spark RDD implementation
- Apache Pig implementation
- Local Spark execution
- Hadoop HDFS and YARN execution
- Docker support
- SQL vs RDD benchmark runner
- Benchmark results written to CSV
- Equivalent SQL and RDD analytical workloads
- JUnit 5 tests
- Maven build configuration

---

## Analytics Jobs

| Job | CLI Value | Description |
|---|---|---|
| Single Record Lookup | `lookup` | Retrieve a taxi trip using multiple business-key filters |
| Rate Code Filter | `filter` | Filter records by `RatecodeID` |
| Payment Type Aggregation | `payment` | Group records by payment type and calculate counts |

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
├── docs/
├── pig/
├── scripts/
├── benchmarks/
├── output/              # generated; ignored by Git
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

### Java Package Structure

```text
com.proapps.taxianalytics
├── benchmark
├── cli
├── config
├── jobs
│   ├── sql
│   └── rdd
├── model
├── parser
├── util
└── exception
```

---

## Dataset

This project uses the publicly available NYC Taxi & Limousine Commission (TLC) Yellow Taxi Trip dataset.

Official dataset:

https://www.nyc.gov/site/tlc/about/tlc-trip-record-data.page

The sample dataset used for local development and testing is located at:

```text
data/sample/yellow_tripdata_sample.csv
```

Use the complete TLC dataset for larger-scale execution and performance evaluation.

### HDFS Dataset Setup

Create the HDFS directory:

```bash
hdfs dfs -mkdir -p /user/data/taxi
```

Upload the dataset:

```bash
hdfs dfs -put yellow_tripdata_*.csv /user/data/taxi/
```

Verify the files:

```bash
hdfs dfs -ls /user/data/taxi
```

---

## Build

Build the project using Maven:

```bash
mvn clean package
```

The application JAR is generated at:

```text
target/taxi-trip-analytics-1.0.0.jar
```

---

## Local Execution

Use the generic Spark helper script to run an analytical workload:

```bash
./scripts/run-spark.sh \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

Change the parameters to select a different processing engine or workload.

### Parameters

| Parameter | Values | Description |
|---|---|---|
| `--engine` | `sql`, `rdd` | Spark processing API |
| `--job` | `lookup`, `filter`, `payment` | Analytical workload |
| `--input` | CSV path | Input dataset |
| `--output` | Directory path | Output location |

To use the RDD implementation, change:

```text
--engine sql
```

to:

```text
--engine rdd
```

and use an appropriate output path, for example:

```text
--output output/payment-rdd
```

To select another workload, change `--job` to one of:

```text
lookup
filter
payment
```

The same command structure is used for all SQL and RDD workloads.

---

## Benchmark

The project includes a benchmark runner for comparing equivalent Spark SQL and Spark RDD workloads.

The benchmark executes:

| Workload | Spark SQL | Spark RDD |
|---|---|---|
| Single Record Lookup | Yes | Yes |
| Rate Code Filter | Yes | Yes |
| Payment Type Aggregation | Yes | Yes |

Execution time is measured in milliseconds.

### Run Benchmark

```bash
./scripts/run-benchmark.sh
```

The script builds the application and executes all SQL and RDD analytical workloads against the sample dataset.

Benchmark mode uses:

```text
--engine benchmark
--job all
--input data/sample/yellow_tripdata_sample.csv
--output output/benchmark
```

### Benchmark Output

Benchmark results are written to:

```text
output/benchmark/benchmark-results.csv
```

The CSV contains the execution time for each workload:

```csv
workload,time_ms
sql.lookup,<time>
sql.filter,<time>
sql.payment,<time>
rdd.lookup,<time>
rdd.filter,<time>
rdd.payment,<time>
```

Generated benchmark results are excluded from version control.

> **Note:** Benchmark timings vary depending on the dataset, machine resources, Spark/JVM startup overhead, filesystem state, and runtime environment. Use repeated runs under the same conditions for meaningful performance comparisons.

---

## SQL and RDD Equivalence

The Spark SQL and Spark RDD implementations perform equivalent analytical operations.

For example, Payment Type Aggregation:

1. Reads the Yellow Taxi Trip CSV data.
2. Extracts the payment type.
3. Groups records by payment type.
4. Counts records for each payment type.
5. Writes the aggregation result.

Example SQL output:

```text
payment_type,count
1,2
```

Equivalent RDD output:

```text
1,2
```

The SQL/DataFrame writer includes a CSV header while the RDD output contains the analytical data rows.

Compare the analytical values with:

```bash
diff \
  <(tail -n +2 output/payment-sql/part-*) \
  <(cat output/payment-rdd/part-*)
```

No output from `diff` indicates that the SQL and RDD analytical values match.

---

## Output

Spark writes analytical results as distributed output directories.

Example:

```text
output/
├── payment-sql/
│   ├── _SUCCESS
│   └── part-00000-*.csv
└── payment-rdd/
    ├── _SUCCESS
    └── part-00000-*
```

Inspect a result with:

```bash
cat output/payment-sql/part-*
```

Change the path to inspect another engine or workload.

The `output/` directory contains generated runtime data and is excluded from version control.

---

## Docker

Build and run the Docker environment:

```bash
./scripts/run-docker.sh
```

Equivalent command:

```bash
docker compose up --build --remove-orphans
```

The Docker configuration provides:

- Java 17
- Apache Spark 3.5.1
- The packaged application JAR
- The sample dataset

To run an analytical workload directly:

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine sql \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-sql
```

Change `--engine`, `--job`, and `--output` in the same way as local execution.

Docker-generated output is written under:

```text
/app/output/
```

When the output directory is mounted to the host, the results are available under the local `output/` directory.

---

## Hadoop / YARN

The application can also run on a Hadoop cluster using HDFS and YARN.

After uploading the dataset to HDFS, submit a workload with:

```bash
spark-submit \
  --master yarn \
  --deploy-mode client \
  --class com.proapps.taxianalytics.cli.Application \
  target/taxi-trip-analytics-1.0.0.jar \
  --engine sql \
  --job payment \
  --input /user/data/taxi \
  --output /user/output/payment-sql
```

Change `--engine`, `--job`, `--input`, and `--output` to run other workloads.

Verify the output:

```bash
hadoop fs -ls /user/output/payment-sql
```

Inspect the result:

```bash
hadoop fs -cat /user/output/payment-sql/part*
```

---

## Apache Pig

Equivalent Apache Pig implementations are provided for the analytical workloads:

```text
pig/
├── single-record-lookup.pig
├── ratecode-filter.pig
└── payment-type-analytics.pig
```

The Pig scripts read input data from HDFS and write analytical results back to HDFS.

For example:

```bash
pig pig/payment-type-analytics.pig
```

The Apache Pig implementations are separate from the Java benchmark runner.

The current benchmark runner compares Spark SQL and Spark RDD workloads.

---

## Scripts

Helper scripts are available under:

```text
scripts/
├── run-benchmark.sh
├── run-docker.sh
└── run-spark.sh
```

### `run-spark.sh`

Generic local Spark launcher:

```bash
./scripts/run-spark.sh \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

Change the parameters to run another SQL or RDD workload.

### `run-benchmark.sh`

Builds the application and runs the complete SQL/RDD benchmark:

```bash
./scripts/run-benchmark.sh
```

### `run-docker.sh`

Builds and starts the Docker Compose environment:

```bash
./scripts/run-docker.sh
```

---

## Testing

The project uses JUnit 5 for automated testing.

Run the complete test suite:

```bash
mvn test
```

---

## Performance Evaluation

The benchmark runner compares equivalent Spark SQL and Spark RDD workloads using the same input dataset.

For meaningful performance comparisons:

- Use the same dataset for each implementation.
- Use the same machine and Spark configuration.
- Run benchmarks multiple times.
- Account for JVM and Spark startup overhead.
- Record the environment and dataset size when publishing results.

Benchmark results are generated at runtime under:

```text
output/benchmark/
```

Additional performance documentation is available in:

```text
docs/Performance-Comparison.md
```

---

## Roadmap

- Additional Spark analytical workloads
- Parameterized Apache Pig scripts
- Repeated benchmark runs and aggregate statistics
- Automated benchmark reporting
- Apache Spark standalone deployment
- Amazon EMR deployment
- Extended integration testing

---

## License

This project is licensed under the MIT License.