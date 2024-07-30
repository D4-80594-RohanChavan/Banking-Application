FROM openjdk:17-alpine
WORKDIR /opt
ENV PORT 7082
EXPOSE 7082
COPY *.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar