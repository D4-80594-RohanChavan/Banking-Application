FROM openjdk:17-alpine
WORKDIR /opt
ENV PORT 7078
EXPOSE 7078
COPY *.jar /opt/reportingservice.jar
ENTRYPOINT exec java $JAVA_OPTS -jar reportingservice.jar