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

# Expor a porta da aplicação
EXPOSE 8080

# Executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
