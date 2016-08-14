The following external Docker infrastructure components are required for running the application stack and included in [infrastructure](infrastructure):

* [docker-zookeeper (3.4.6)](infrastructure/docker-zookeeper)
Source: https://github.com/wurstmeister/zookeeper-docker

* [docker-kafka (0.9.0.1)](infrastructure/docker-kafka)
Source: https://github.com/wurstmeister/kafka-docker

Modifications: use Kafka 0.9.1, installation of collector-client

* [docker-flink (1.0.3)](infrastructure/docker-flink)
Source: https://github.com/apache/flink/tree/master/flink-contrib/docker-flink

Modifications: enable remote JMX connection, installation of collector-client

* [docker-elk (Elasticsearch 2.3.3, Logstash 2.3.2, and Kibana 4.5.1)](infrastructure/docker-elk)
Source: https://github.com/spujadas/elk-docker

Modifications: Kafka input configuration
