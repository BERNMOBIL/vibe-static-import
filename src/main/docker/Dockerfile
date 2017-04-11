FROM openjdk:8
VOLUME /tmp
ADD staticImport-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]