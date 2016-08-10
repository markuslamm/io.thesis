FROM anapsix/alpine-java
MAINTAINER markus.lamm@googlemail.com
VOLUME /tmp
ADD collector-manager-app/target/collector-manager-app.jar /apps/collector-manager-app.jar
RUN bash -c 'touch /apps/collector-manager-app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/apps/collector-manager-app.jar"]