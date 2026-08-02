-- Load the input files
input_file = LOAD '/user/ec2-user/spark_assignment/input/yellow_tripdata_*'
USING PigStorage(',')
AS (
    VendorID:chararray,
    tpep_pickup_datetime:chararray,
    tpep_dropoff_datetime:chararray,
    passenger_count:int,
    trip_distance:double,
    RatecodeID:int,
    store_and_fwd_flag:chararray,
    PULocationID:int,
    DOLocationID:int,
    payment_type:chararray,
    fare_amount:float,
    extra:float,
    mta_tax:float,
    tip_amount:float,
    tolls_amount:float,
    improvement_surcharge:float,
    total_amount:float
);

-- Remove header
ranked = RANK input_file;
data = FILTER ranked BY (rank_input_file > 1);

-- Group by payment type
grouped = GROUP data BY payment_type;

-- Count records
counted = FOREACH grouped GENERATE group, COUNT(data);

-- Sort by count descending
sorted_counted = ORDER counted BY $1 DESC;

-- Store result
STORE sorted_counted INTO '/user/ec2-user/pig/output/grouped_output';