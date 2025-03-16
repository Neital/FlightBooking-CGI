FROM openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the build output (JAR file) into the container
COPY build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the environment variables for DB connection
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/flight_booking
ENV SPRING_DATASOURCE_USERNAME=flight_user
ENV SPRING_DATASOURCE_PASSWORD=CGIPraktika

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]