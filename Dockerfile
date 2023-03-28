FROM eclipse-temurin:11
VOLUME /Users/ken/Desktop/test
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]