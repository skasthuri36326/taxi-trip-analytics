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

The SQL and RDD implementations perform equivalent analytical operations, allowing their results and execution times to be compared.

---

## Features

- Apache Spark SQL / DataFrame implementation
- Apache Spark RDD implementation
- Apache Pig implementation
- Local Spark execution
- Hadoop HDFS and YARN execution
- Docker support
- SQL vs RDD benchmark runner
- CSV benchmark result generation
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
├── output/
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

The project uses the publicly available NYC Taxi & Limousine Commission (TLC) Yellow Taxi Trip dataset.

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

Build the project:

```bash
mvn clean package
```

The application JAR is generated at:

```text
target/taxi-trip-analytics-1.0.0.jar
```

---

## Local Execution

Use the generic Spark helper script to run SQL or RDD workloads:

```bash
./scripts/run-spark.sh \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

Change the parameters to run a different engine or workload.

### Parameters

| Parameter | Values | Description |
|---|---|---|
| `--engine` | `sql`, `rdd` | Spark processing API |
| `--job` | `lookup`, `filter`, `payment` | Analytical workload |
| `--input` | CSV path | Input dataset |
| `--output` | Directory path | Output location |

For example, to run the same workload using RDD, change:

```text
--engine sql
```

to:

```text
--engine rdd
```

and change the output directory accordingly:

```text
--output output/payment-rdd
```

To run another workload, change:

```text
--job payment
```

to either:

```text
--job lookup
```

or:

```text
--job filter
```

---

## Benchmark

The benchmark runner compares equivalent Spark SQL and Spark RDD implementations.

It executes:

| Workload | SQL | RDD |
|---|---|---|
| Single Record Lookup | Yes | Yes |
| Rate Code Filter | Yes | Yes |
| Payment Type Aggregation | Yes | Yes |

Execution time is measured in milliseconds.

### Run Benchmark

```bash
./scripts/run-benchmark.sh
```

The script builds the application and runs the complete SQL/RDD benchmark suite against the sample dataset.

Equivalent application parameters are:

```text
--engine benchmark
--job all
--input data/sample/yellow_tripdata_sample.csv
--output output/benchmark
```

### Benchmark Output

Example console output:

```text
Benchmark timings (ms):
sql.lookup = 1611
sql.filter = 191
sql.payment = 466
rdd.lookup = 120
rdd.filter = 66
rdd.payment = 136
```

The benchmark results are also written to:

```text
output/benchmark/benchmark-results.csv
```

Example:

```csv
workload,time_ms
sql.lookup,1611
sql.filter,191
sql.payment,466
rdd.lookup,120
rdd.filter,66
rdd.payment,136
```

The `output/` directory contains generated runtime results and is excluded from version control.

> **Note:** Benchmark timings depend on machine resources, JVM and Spark startup overhead, filesystem state, dataset size, and runtime conditions. Use repeated runs under the same environment for meaningful performance comparisons.

---

## SQL and RDD Equivalence

The SQL and RDD implementations perform equivalent analytical operations.

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

The SQL/DataFrame writer includes a CSV header while the RDD implementation writes the analytical data rows.

Compare the values with:

```bash
diff \
  <(tail -n +2 output/payment-sql/part-*) \
  <(cat output/payment-rdd/part-*)
```

No output from `diff` indicates that the SQL and RDD analytical values match.

---

## Output Format

Spark writes results as distributed output directories.

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

Inspect a result using:

```bash
cat output/payment-sql/part-*
```

Change the path to inspect another workload or engine.

Generated files under `output/` are excluded from version control.

---

## Docker Execution

Build and run the Docker environment:

```bash
./scripts/run-docker.sh
```

Equivalent command:

```bash
docker compose up --build --remove-orphans
```

The Docker configuration provides the Java and Spark runtime, application JAR, and sample dataset.

To run a workload directly:

```bash
docker run --rm \
  taxi-trip-analytics \
  --engine sql \
  --job payment \
  --input /app/data/sample/yellow_tripdata_sample.csv \
  --output /app/output/payment-sql
```

Change `--engine`, `--job`, and `--output` in the same way as local execution.

---

## Hadoop / YARN Execution

The same application can run on a Hadoop cluster using HDFS and YARN.

After uploading the dataset to HDFS, submit a job with:

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

Inspect the result with:

```bash
hadoop fs -cat /user/output/payment-sql/part*
```

---

## Apache Pig

Equivalent Apache Pig implementations are available for the three analytical workloads:

```text
pig/
├── single-record-lookup.pig
├── ratecode-filter.pig
└── payment-type-analytics.pig
```

For example:

```bash
pig pig/payment-type-analytics.pig
```

The Pig implementations operate against HDFS and are separate from the Java SQL/RDD benchmark runner.

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

Generic local Spark launcher.

```bash
./scripts/run-spark.sh \
  --engine sql \
  --job payment \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/payment-sql
```

Change the parameters to run other SQL or RDD workloads.

### `run-benchmark.sh`

Runs the complete SQL/RDD benchmark:

```bash
./scripts/run-benchmark.sh
```

### `run-docker.sh`

Builds and starts the Docker environment:

```bash
./scripts/run-docker.sh
```

---

## Testing

The project uses JUnit 5.

Run the test suite:

```bash
mvn test
```

---

## Performance Evaluation

The benchmark runner compares equivalent Spark SQL and Spark RDD workloads using the same input dataset.

Measurements include:

- Execution time
- Processing engine
- Analytical workload

For meaningful comparisons:

- Use the same dataset.
- Use the same Spark configuration.
- Run each benchmark multiple times.
- Account for JVM and Spark startup overhead.
- Record the machine and dataset size when publishing results.

Additional information is available in:

```text
docs/Performance-Comparison.md
```

---

## Roadmap

- Additional Spark SQL analytical workloads
- Parameterized Apache Pig scripts
- Repeated benchmark runs and aggregate statistics
- Automated benchmark reporting
- Apache Spark standalone deployment
- Amazon EMR deployment
- Extended integration testing

---

## License

This project is licensed under the MIT License.