## Run the application:

###### Build the application sources:

`mvn clean install -Dmaven.test.skip=true -DskipTests` 
//TODO exclude IT on install

###### Build required Docker images:

`./build-docker-images.sh`

Builds the required Docker images for

* Apache Zookeeper
* Apache Kafka
* Apache Flink
* ELK
* collector-server

This will  may take a while, depending on your internet connection.


###### Start application:

`docker-compose up`

This will startup containers for Flink-JobManager/Taskmanager, Kafka, ELK and the collector-server.

* Go to collector-server UI: http://{DOCKER-HOST}:9090. 
There should be three registered clients running on Flink-JobManager/Taskmanager and the Kafka broker.


