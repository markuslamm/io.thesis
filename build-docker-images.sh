#!/bin/sh

echo "cleanup containers..."
docker rm consul
docker rm collector-manager

echo "building sources..."
mvn clean install -Dmaven.test.skip=true -DskipTests

echo "building collector-manager-app..."
docker build -t io.thesis/collector-manager collector-manager

