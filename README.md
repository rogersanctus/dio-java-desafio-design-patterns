# Projeto para desafio dio.me - Java Design Patterns

## O desafio:

Criar uma aplicação Java, pode ser com Spring Boot, que aplique os conceitos de Design Patterns.

## Sobre a Aplicação

A aplicação tem como objetivo demonstrar o uso de design patterns em uma aplicação Java com Spring Boot.

A ideia do projeto é uma API fictícia de recebimento de pagamentos via PIX. Podem existir vários serviços que façam a intermediação dos pagamentos, e cada um deles pode ter uma faixa de tarifas diferentes.
E cada usuário do sistema recebe uma cobrança de tarifas dependendo do seu volume de vendas. Quando o usário inicia uma cobrança pela API, o sistema decide qual serviço externo será utilizado de acordo
com a tarifa deste usuário.

## Rodando o projeto:

Se não tiver o gradle instalado, execute o comando abaixo:

```bash
./gradlew bootRun
```

Caso esteja em ambiente Windows, adicione a extensão `.bat` ao comando:

```bash
./gradlew.bat bootRun
```

Se modificar algo na estrutura do projeto, pode ser necessário limpar e fazer build novamente:

```bash
./gradlew clean && ./gradlew build
```
