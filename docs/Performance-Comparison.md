# Performance Comparison

The following benchmark results were recorded on the same dataset and environment during earlier runs of the project:

| Workload | Apache Pig | Spark RDD |
| --- | ---: | ---: |
| Single Record Lookup | 8m 04s | 1m 36s |
| Rate Code Filter | 8m 02s | 1m 35s |
| Group By + Order By | 11m 19s | 1m 36s |

These reference numbers are included for context only. Use the benchmark runner in the repository to record fresh timings for your own machine, Docker environment, or Spark cluster.
