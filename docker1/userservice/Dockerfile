FROM openjdk:17-alpine
WORKDIR /opt
ENV PORT 7070
EXPOSE 7070
COPY *.jar /opt/reportingservice.jar
ENTRYPOINT exec java $JAVA_OPTS -jar reportingservice.jar