FROM maven:3.9-eclipse-temurin-17
WORKDIR /app

# Copy project files
COPY . .

# Compile Java and download dependencies
RUN mvn clean compile

# Port dynamically set by cloud providers (Render, Railway, Heroku)
ENV PORT=8080
EXPOSE 8080

CMD ["mvn", "exec:java"]
