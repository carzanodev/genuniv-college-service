FROM openjdk:11-slim
ADD target/genuniv-college-service.jar /jar/genuniv-college-service.jar
EXPOSE 19101
ENTRYPOINT ["java", "-jar", "/jar/genuniv-college-service.jar"]
