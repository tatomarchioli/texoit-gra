# Golden Raspberry Award
API Spring Boot para consulta dos produtores com maiores e menores intervalos entre dois prêmios Golden Raspberry Award.

### Pré-Requisitos
Para executar este projeto, você vai precisar de:
* `Java JDK v17.0.1` ou superior;
* `Apache Maven v3.8.1` ou superior;

Para executar o projeto por meio de uma IDE, você vai precisar de:
* `Lombok plugin` para IDE de sua escolha;

### Preparando o ambiente
Antes de executar os testes ou subir a aplicação, é necessário realizar a instalação das dependências.
Para isso, acesso a pasta raiz da aplicação por meio de um terminal a sua escolha, e execute o comando `mvn install`.

### Executando os testes de integração
Para executar os testes de integração, acesse a pasta raiz da aplicação por meio de um terminal a sua escolha, e execute o comando `mvn test`.
Após a finalização dos testes, os resultados serão exibidos no console.

### Subindo a aplicação em ambiente local
Para iniciar a API em ambiente local, acesse a pasta raiz da aplicação por meio de um terminal a sua escolha, e execute o comando `mvn spring-boot:run`. 
Após a finalização do build e a execução do projeto, a aplicação estará disponível na porta local `8080`.

## Acessando a API
Após executar o deploy da aplicação, para realizar a consulta dos produtores, acesse o endpoint `/api/producers/fastest-and-slowest-winners` (HTTP GET). 

A API responderá com o status HTTP `200`, retornando um corpo do tipo `application/json`.

Este endpoint disponibiliza o parâmetro `limit` (parâmetro de query) que recebe um valor inteiro positivo, e controla a quantidade de produtores que será retornada em cada lista (`min` e `max`). Exemplo de utilização: `/api/producers/fastest-and-slowest-winners?limit=4`.







