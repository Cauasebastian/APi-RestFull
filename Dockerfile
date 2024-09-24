# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Definir diretório de trabalho
WORKDIR /app

# Definir variáveis de ambiente para UTF-8
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

# Copiar o pom.xml e baixar as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar o código-fonte
COPY src ./src

# Construir o pacote
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-alpine

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR construído da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Definir as variáveis de ambiente para o Spring Boot
ENV SPRING_APPLICATION_NAME=generationbrazil
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://autorack.proxy.rlwy.net:44115/railway
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=ZscVrbwOnzQiOXUioTPazRyklkcVAAVq
ENV SPRING_PROFILES_ACTIVE=dev

# Expor a porta da aplicação
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
