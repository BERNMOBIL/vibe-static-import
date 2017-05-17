FROM openjdk:8-alpine
VOLUME /tmp
ADD staticimport-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
CMD ["java", "-jar", "app.jar"]