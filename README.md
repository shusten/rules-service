# Rules-Service

Microserviço responsável pelas regras de rastreabilidade e autorização de pessoas baseado em eventos de reconhecimento facial.

## 📋 Descrição

O `rules-service` recebe requisições da API **Stream-Capturer** informando o identificador de uma pessoa reconhecida e o local (câmera).  
A partir desse evento, o serviço pode consultar o banco de dados, aplicar regras de negócio (autorização, bloqueio, etc) e notificar clientes conectados via WebSocket.

## ⚡ Tecnologias

- Java 21
- Spring Boot 3.4+
- Spring WebFlux (reativo)
- WebSocket (STOMP)
- Lombok
- R2DBC + Driver SQL Server (ajustável para outros bancos)