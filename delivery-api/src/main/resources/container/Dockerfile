FROM openjdk:21

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR para o diretório de trabalho
COPY target/*.jar app.jar

# Expõe a porta padrão da aplicação (ajuste se necessário)
EXPOSE 8080

# Define o ponto de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]