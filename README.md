## io.thesis [![Build Status](https://travis-ci.org/markuslamm/io.thesis.png?branch=master)](https://travis-ci.org/markuslamm/io.thesis)

The repository contains all artifacts of my bachelor's thesis

##### Design and Implementation of a Tool to Collect Execution- and Service-Data of Big Data Analytics Applications

in Media Informatics at the Department VI of the Beuth University of Applied Sciences Berlin.

The main objective is to create a fast and scalable software-architecture for collecting and storing system and application data on the example of Apache Flink.

* [Prerequisites](md/prerequisites.md)
* [Software environment](md/sw-environment.md)
* [Required infrastructure components](md/external-docker-infrastructure.md)
* [System architecture](md/system-architecture.md)
* [Class diagrams](md/class-diagrams.md)
* [Deployment](md/deployment.md)
* [Run application stack](md/run-application.md)

Components:

* [collector-client](collector-client/README.md)
* [collector-manager](collector-manager/README.md)
* [collector-data-p rocessor](collector-data-processor/README.md)

The following Collector implementations are available:

* [jvm-collector](collectors/jvm-jmx-collector/README.md)
* [dstat-collector](collectors/dstat-collector/README.md)
* [flink-jmx-collector](collectors/flink-jmx-collector/README.md)
* [flink-rest-collector](collectors/flink-rest-collector/README.md)
* [kafka-broker-jmx-collector](collectors/kafka-broker-jmx-collector/README.md)