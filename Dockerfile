# Use a Java 17 base image
FROM openjdk:17-slim

# Set working directory
WORKDIR /app

# Copy your project into the container
COPY . .

# Make gradlew executable (important for Linux)
RUN chmod +x gradlew

# Build the project
RUN ./gradlew build

# Set the command to run your app
CMD ["java", "-jar", "build/libs/demo-0.0.1-SNAPSHOT.jar"]
