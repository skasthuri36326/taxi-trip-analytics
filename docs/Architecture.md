# Architecture

The project is structured as a small analytics application with a shared CLI and multiple execution engines.

## Layers

- **cli**: parses command line arguments and dispatches jobs
- **config**: builds Spark sessions and shared configuration
- **jobs.sql**: Spark SQL / DataFrame implementations
- **jobs.rdd**: Spark RDD implementations
- **parser**: lightweight CSV parsing helpers and validation
- **model**: small domain objects used by the jobs
- **benchmark**: timing and comparison utilities
- **util**: reusable helper functions

## Data flow

1. Read NYC TLC Yellow Taxi CSV data
2. Remove or ignore malformed rows
3. Execute the requested workload
4. Write the results to a destination directory
5. Optionally benchmark the runtime

## Design choice

Spark SQL / DataFrames is the primary implementation because it is concise, maintainable, and aligned with current Spark usage. Spark RDD is retained as a baseline implementation for comparison and for lower-level processing examples.
