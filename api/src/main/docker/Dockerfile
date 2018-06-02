FROM anapsix/alpine-java:8
MAINTAINER shboland <sybboland@gmail.com>

VOLUME /tmp
COPY *.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
