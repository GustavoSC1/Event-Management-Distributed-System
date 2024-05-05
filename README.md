# Event Management System - Microservices
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/GustavoSC1/Event-Management-Distributed-System/blob/main/LICENSE)

Este é um sistema de gerenciamento de eventos desenvolvido com uma arquitetura de microsserviços. O sistema permite que os usuários criem, participem e gerenciem eventos.

O projeto é uma API REST criada usando a linguagem Java, o banco de dados PostgreSQL, os projetos do ecossistema Spring e outras tecnologias.

## Visão Geral

O sistema é composto por vários microsserviços independentes, cada um com uma responsabilidade específica e que interagem entre si por meio de comunicação Síncrona e Assíncrona:

- **User Service -** Gerencia todas as funcionalidades relacionadas aos usuários do sistema. Isso inclui a criação, atualização, exclusão e recuperação de informações sobre usuários.
- **Event Service -** Lida com todas as funcionalidades relacionadas aos eventos. Isso inclui a criação, atualização, consulta e recuperação de eventos. Também é responsável pela reserva e gerenciamento de tickets.
- **Notification Service -** Salva e envia as notificações para os usuários: como confirmação de inscrição em eventos, confirmação de pagamento de tickets, atualizações sobre os eventos que o usuário está participando, etc.
- **Payment Service -** Representa algumas funcionalidades relacionadas ao processamento de pagamentos. 

## Arquitetura
![Arquitetura 1](https://ik.imagekit.io/gustavosc/Event%20Management%20Distributed%20System/Diagrama%20sem%20nome(4).drawio_jUoj0-pj-.png?updatedAt=1714907467108)
![Modelo Conceitual](https://ik.imagekit.io/gustavosc/Event%20Management%20Distributed%20System/Event%20Management_h8wv6ATdE.png?updatedAt=1712266532493)

## Tecnologias Utilizadas

- [Java 17](https://www.oracle.com/java/)
- [PostgreSQL](https://www.postgresql.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring AMQP](https://spring.io/projects/spring-amqp)
- [Spring Hateoas](https://spring.io/projects/spring-hateoas)
- [Spring Security](https://spring.io/projects/spring-security)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/)
- [Spring Cloud Eureka](https://cloud.spring.io/spring-cloud-netflix/reference/html/)
- [Spring Cloud Circuitbreaker](https://spring.io/projects/spring-cloud-circuitbreaker)
- [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
- [Spring Cloud Config](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
- [Resilience4j](https://resilience4j.readme.io/docs/getting-started)
- [Zipkin](https://zipkin.io/)
- [Elasticsearch](https://www.elastic.co/pt/elasticsearch)
- [Kibana](https://www.elastic.co/pt/kibana)
- [Filebeat](https://www.elastic.co/pt/beats/filebeat)
- [OpenAPI](https://swagger.io/specification/)
- [Maven](https://maven.apache.org/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [Docker](https://www.docker.com/)

## Executando o Projeto Localmente

1. Clone o repositório para o seu ambiente local: `https://github.com/GustavoSC1/Event-Management-Distributed-System.git`
2. Navegue até o diretório raiz do projeto: `cd Event-Management-Distributed-System`
3. Execute `docker-compose up -d` para iniciar o RabbitMQ, Zipkin e os Bancos de dados.
4. Entre em cada pasta `mvn clean verify -DskipTests` para construir os microsserviços.
5. Depois disso execute `mvn spring-boot:run` entrando em cada pasta para iniciar os microsserviços.

## Contribuindo

Contribuições são bem-vindas! Se você quiser contribuir com melhorias ou correções para o projeto, siga estes passos:

1. Fork o repositório
2. Crie uma branch para a sua funcionalidade (`git checkout -b feature/sua-funcionalidade`)
3. Faça commit das suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Faça push para a branch (`git push origin feature/sua-funcionalidade`)
5. Abra um Pull Request

## Autor

Gustavo da Silva Cruz

https://www.linkedin.com/in/gustavo-silva-cruz-20b128bb/
