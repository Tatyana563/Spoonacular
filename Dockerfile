# ---------- Stage 1: Build the application ----------
FROM bellsoft/liberica-runtime-container:jdk-17-musl as builder

WORKDIR /home/app

# Add the source code into the build container
ADD . /home/app/spoonacular

# Ensure mvnw is executable
RUN chmod +x /home/app/spoonacular/mvnw

# Build the Spring Boot app (skip tests for speed)
RUN cd spoonacular && ./mvnw -Dmaven.test.skip=true clean package

# ---------- Stage 2: Extract JAR layers ----------
FROM bellsoft/liberica-runtime-container:jdk-17-musl as optimizer

WORKDIR /home/app

# Copy the built JAR from the builder stage
COPY --from=builder /home/app/spoonacular/target/*.jar app.jar

# Use Spring Boot layertools to extract layers
RUN java -Djarmode=layertools -jar app.jar extract

# ---------- Stage 3: Minimal runtime image ----------
FROM bellsoft/liberica-runtime-container:jre-17-musl

WORKDIR /app

# Copy individual layers for optimized caching
COPY --from=optimizer /home/app/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-loader/ ./
COPY --from=optimizer /home/app/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/application/ ./

# Expose the application port
EXPOSE 8080

# Start the application using Spring Boot’s layered launcher
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
