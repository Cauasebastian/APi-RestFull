# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Definindo as variáveis de ambiente
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/aluno_db
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=password
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
