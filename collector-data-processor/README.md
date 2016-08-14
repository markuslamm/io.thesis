### collector-data-processor

A Apache Flink stream processing application that receives raw collector data from Kafka,filters for data from 'flink_jmx' collector, 
extracts contained CPU data and stores flat data in 'flink-job-index' for create a basic Kibana visualization for prototyping purpose.

