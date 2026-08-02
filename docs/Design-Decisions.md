# Design Decisions

## Why Spark SQL first?

Spark SQL and DataFrames are generally easier to maintain than raw RDD code and fit well with current Spark usage patterns.

## Why keep Spark RDD?

Spark RDD is retained as a separate implementation so the same workloads can be expressed with a lower-level API and compared against the SQL/DataFrame approach.

## Why keep Apache Pig?

The Pig scripts provide a framework comparison point and document the same analytics workloads using a legacy batch-processing style.

## Why use string-based columns?

The included data sample and TLC CSV files are naturally schema-driven, but keeping the columns as strings keeps the jobs tolerant of missing or inconsistent values. The jobs convert values only when needed.
