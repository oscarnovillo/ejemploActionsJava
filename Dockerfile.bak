# Etapa 1: Construir la aplicación usando Maven con JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el pom.xml primero para aprovechar la caché de Docker para las dependencias
COPY pom.xml .

# Descargar dependencias
# Si usas el wrapper de Maven (mvnw), descomenta las siguientes dos líneas y comenta la de abajo
# COPY mvnw .
# COPY .mvn .mvn
# RUN ./mvnw dependency:go-offline
RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src ./src

# Construir la aplicación, omitiendo los tests
# Si usas el wrapper de Maven (mvnw), descomenta la siguiente línea y comenta la de abajo
# RUN ./mvnw package -DskipTests
RUN mvn package -DskipTests

# Etapa 2: Crear la imagen final usando un JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
# Ajusta el nombre del JAR si es diferente en tu pom.xml o si se generan varios JARs
# El nombre del JAR en tu target es spring_rest_docker.jar
COPY --from=build /app/target/spring_rest_docker.jar app.jar

# Exponer el puerto en el que corre la aplicación (asumiendo 8080 por defecto para Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
