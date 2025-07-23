## Desafio Técnico: Serviço de Gerenciamento de Perfis
do GitHub

## Descrição Geral

O objetivo é implementar um serviço utilizando SPRING para o gerenciamento de
usuários do GITHUB, disponibilizando APIs REST para o controle de perfis.

## 2. Requisitos Obrigatórios

## 2.1. Sincronização e Persistência de Dados

●​ O serviço deve realizar a busca de 30 usuários do GITHUB através da API​
GET https://api.github.com/users. 

●​ Os dados obtidos devem ser salvos em uma base de dados, que pode ser local
ou na nuvem.
●​ A escolha da tecnologia do banco de dados é livre.

## 2.2. Modelo de Dados

O modelo de dados deve permitir que um usuário tenha um ou mais perfis atribuídos.
As entidades mínimas a serem salvas são: 

User
●​ id: PK, BIGSERIAL
●​ login: TEXT NOT NULL
●​ url: TEXT NOT NULL

Role
●​ id: PK, BIGSERIAL
●​ name: TEXT NOT NULLUserRole (Tabela de Associação)
●​ id: PK, BIGSERIAL
●​ user_id: FK, BIGINT NOT NULL
●​ role_id: FK, BIGINT NOT NULL

## 2.3. APIs REST

As seguintes APIs devem ser implementadas:

●​ Criar um novo perfil.
●​ Atribuir um perfil a um usuário.
●​ Listar todos os usuários com a lista de perfis vinculados.

## 3. Recomendações Técnicas

●​ Persistência: Recomenda-se utilizar o conceito de Hibernate e JPA, relacionando
as entidades com a anotação @ManyToMany.
●​ Arquitetura: A aplicação deve ser estruturada em camadas (Controller, Service,
DTO/Entity e Repository/Database) para cada módulo.

## 4. Requisitos de Teste

●​ É necessário implementar ao menos um teste unitário para a camada de
Controller e para a camada de Service.

## 5. Pontos Extras (Opcional)

A implementação dos seguintes itens será considerada um diferencial:
●​ Utilizar o conceito de DB migration do Spring Boot.
●​ Realizar a autenticação das APIs via token, utilizando o cabeçalho HTTP​
Authorization.
