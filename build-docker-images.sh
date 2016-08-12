#!/bin/sh

echo "cleanup containers..."
docker rm consul collector-manager flink-jobmanager flink-taskmanager kafka elk 1>/dev/null 2>&1

echo "building sources..."
mvn install -DskipITs

echo "building collector-manager-app..."
docker build -t io.thesis/collector-manager collector-manager

echo "building docker-base..."
docker build -t wurstmeister/base infrastructure/docker-base

echo "building docker-zookeeper..."
docker build -t wurstmeister/zookeeper infrastructure/docker-zookeeper

echo "building docker-kafka..."
docker build -t wurstmeister/kafka infrastructure/docker-kafka

echo "building docker-elk..."
docker build -t sebp/elk infrastructure/docker-elk

echo "building docker-flink..."
cd infrastructure/docker-flink
./build.sh

