FROM openjdk:17-alpine
WORKDIR /opt
ENV PORT 7074
EXPOSE 7074
COPY *.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar