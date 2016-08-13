#!/bin/sh

echo "building sources..."
mvn install -DskipITs

echo "cleanup containers..."
docker rm -f $(docker ps -a -q)

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

