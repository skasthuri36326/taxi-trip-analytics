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

-- Apply the filter condition
data = FILTER input_file BY
    tpep_pickup_datetime == '2017-10-01 00:15:30'
    AND tpep_dropoff_datetime == '2017-10-01 00:25:11'
    AND VendorID == '2'
    AND passenger_count == 1
    AND trip_distance == 2.17;

-- Store the result
STORE data INTO '/user/ec2-user/pig/output/single_row_lookup';