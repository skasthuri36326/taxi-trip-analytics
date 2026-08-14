#!/usr/bin/env bash
set -euo pipefail

spark-submit \
  --class com.proapps.taxianalytics.cli.Application \
  --master 'local[*]' \
  target/taxi-trip-analytics-1.0.0.jar \
  "$@"