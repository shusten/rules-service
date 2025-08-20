# Estágio 1: Build - Usa uma imagem Maven com JDK para compilar o projeto.
# Isso mantém as ferramentas de build fora da imagem final.
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de definição do projeto (pom.xml) primeiro.
# O Docker armazena em cache essa camada, então as dependências só são baixadas novamente se o pom.xml mudar.
COPY pom.xml .

# Baixa as dependências do projeto
RUN mvn dependency:go-offline

# Copia o código-fonte da aplicação
COPY src ./src

# Executa o build do Maven para gerar o arquivo .jar
# -DskipTests pula a execução dos testes unitários durante o build da imagem.
RUN mvn package -DskipTests


# Estágio 2: Run - Usa uma imagem leve, apenas com o Java Runtime.
# A aplicação não precisa do Maven ou do JDK completo para rodar.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Argumento para especificar o caminho do JAR a ser copiado.
ARG JAR_FILE=target/*.jar

# Copia o arquivo .jar gerado no estágio de build para a imagem final.
COPY --from=build /app/${JAR_FILE} app.jar

# Expõe a porta 8080 para que a aplicação possa receber requisições.
EXPOSE 8080

# Comando que será executado quando o container iniciar.
# Inicia a aplicação Java.
ENTRYPOINT ["java", "-jar", "app.jar"]