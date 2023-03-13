# Pré-requisitos e instruções para o correto funcionamento da aplicação:

1. Utilizar a versão 17 do Java.
2. Ter o Docker instalado para facilitar a criação do banco de dados e configurações utilizando o **docker-compose.yml** que está na raiz do projeto.
3. Abrir o terminal na raiz do projeto e executar o comando: `docker-compose up -d`
4. Utilizar de preferência o https://www.postman.com/ para testar as requisições. Deixei uma collection pronta na pasta `src/main/resources/collection_postman` para importar já com todos os endpoints criados.
5. Também é possível consultar todos os endpoints criados acessando: http://localhost:8080/swagger-ui/index.html, quando a aplicação estiver em execução.
6. Com o Java 17 instalado, abra o terminal do SO na raiz do projeto e execute o comando: `mvnw clean package` para o maven baixar todas as dependências necessárias e gerar o JAR do projeto.
7. Uma pasta com nome `target` será criada com um arquivo chamado `desafio-votacao-0.0.1-SNAPSHOT.jar`.
8. Para executar o projeto, abra o terminal do SO na pasta `target` e execute o comando: `java -jar desafio-votacao-0.0.1-SNAPSHOT.jar`.
9. Caso o projeto não consiga subir na porta 8080, por estar ocupada por alguma outra aplicação, poderá alterar essa configuração no arquivo `src/main/resources/application-dev.properties` na linha 1, e execute novamente os passos **6, 7 e 8**.

---

# Vantagens em utilizar versionamento da API na URL:

Clareza e transparência: Ao incluir "/v1" na URL da API, fica claro para os usuários que estão usando a versão 1 da API. Isso ajuda a evitar confusões e problemas que podem surgir quando diferentes versões da API estão disponíveis.

Facilidade de manutenção: Ao usar o versionamento na URL, as diferentes versões da API são claramente separadas, o que torna mais fácil manter e atualizar cada versão individualmente.

Controle de compatibilidade: O versionamento permite que os desenvolvedores controlem melhor a compatibilidade da API com diferentes clientes e aplicações. Novas funcionalidades e mudanças significativas podem ser implementadas em novas versões da API, sem afetar a funcionalidade existente para clientes que ainda dependem da versão anterior.

Melhor documentação: Ao versionar a API, os desenvolvedores podem fornecer documentação clara e específica para cada versão, tornando mais fácil para os usuários entenderem as diferenças entre as versões e quais recursos estão disponíveis em cada uma delas.

Suporte a múltiplos clientes: Quando uma API é usada por vários clientes e aplicações, o versionamento permite que cada cliente use a versão mais apropriada da API para suas necessidades específicas, sem afetar outros clientes ou a funcionalidade geral da API.

---