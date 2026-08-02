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

-- Filter RateCodeID = 4
data = FILTER input_file BY RatecodeID == 4;

-- Group all filtered records
DATA_GROUP = GROUP data ALL;

-- Count filtered records
DATA_COUNT = FOREACH DATA_GROUP GENERATE COUNT(data);

-- Store result
STORE DATA_COUNT INTO '/user/ec2-user/pig/output/filtered_output';