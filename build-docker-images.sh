#!/bin/sh

echo "cleanup containers..."
docker rm consul
docker rm collector-manager
docker rm flink-jobmanager
docker rm flink-taskmanager

echo "building sources..."
mvn install -DskipITs

echo "building collector-manager-app..."
docker build -t io.thesis/collector-manager collector-manager

echo "building docker-flink..."
cd infrastructure/docker-flink
./build.sh

