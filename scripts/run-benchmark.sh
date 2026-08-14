#!/usr/bin/env bash
set -euo pipefail

mvn -q -DskipTests package

spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics-1.0.0.jar \
  --engine benchmark \
  --job all \
  --input data/sample/yellow_tripdata_sample.csv \
  --output output/benchmark