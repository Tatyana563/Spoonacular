# Start from an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/spoonacular-0.0.1-SNAPSHOT.jar spoonacular.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8585

# Run the jar file
ENTRYPOINT ["java", "-jar", "spoonacular.jar"]
