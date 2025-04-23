
# Iza Pay

## Sistema de Gestão de Pagamentos

Este sistema foi desenvolvido para simplificar a gestão de pagamentos, oferecendo uma solução eficiente para controlar as finanças da empresa, registrar cobranças e pagamentos, gerenciar previsões de despesas e notificações, e integrar com serviços externos como plataformas de pagamento e envio de mensagens.

### Contribuidores

- [Gleyson Sampaio](https://github.com/glysns)
- [Evandro Silva](https://github.com/Evandrolds)
- [João Pedro](https://github.com/jotape-exe)

## Classes de Modelo

Abaixo estão as principais classes de domínio que representam as entidades e registros do sistema. Cada classe tem uma responsabilidade única que facilita a organização e controle das informações.

### Tabela de Classes de Modelo

| Classe       | Descrição                                                                                          |
|--------------|----------------------------------------------------------------------------------------------------|
| **Empresa**  | Representa os dados da empresa, incluindo informações como nome, CNPJ, endereço e dados de contato. |
| **Configuração** | Armazena as configurações de integração com outros serviços, como plataformas de pagamento e envio de mensagens (ex: WhatsApp). |
| **Cadastro** | Contém o cadastro de clientes e fornecedores, incluindo dados de contato, CPF/CNPJ e outras informações importantes. |
| **Cobrança** | Registra as contas a receber, associadas a vendas ou serviços prestados aos clientes. Inclui valores, prazos e status das cobranças. |
| **Pagamento** | Registra os pagamentos recebidos, tanto avulsos quanto parcelados, podendo estar vinculados a uma cobrança específica. |
| **Previsão** | Representa obrigações periódicas a serem pagas para fornecedores, como contas de água, luz, internet, entre outras. |
| **Despesa** | Registra despesas avulsas ou parcelas de despesas que precisam ser pagas, podendo estar associadas a uma previsão. |
| **Notificação** | Controla as notificações enviadas aos clientes ou fornecedores, podendo ser via e-mail ou WhatsApp. |

## Tecnologias
Devido a algumas alterações do Spring Boot recomendamos utilizar as respectivas versões mecionadas abaixo:

- **Spring Boot 3.2.4**: Framework Java para desenvolvimento de aplicações backend, utilizado para criar APIs REST.
- **Java 17**: Linguagem de programação utilizada no projeto.
- **Spring Data JPA**: Usado para integração com banco de dados, facilitando a persistência de dados.
- **PostgreSQL**: Banco de dados relacional utilizado para armazenar dados.
- **Flyway**: Ferramenta para controle de versão de banco de dados.
- **Lombok**: Biblioteca que ajuda a reduzir o boilerplate code, gerando automaticamente métodos como getters, setters e construtores.
- **Swagger/OpenAPI**: Utilizados para documentação da API REST e interface gráfica (Swagger UI).
- **Spring Security**: Implementação de autenticação e autorização para garantir a segurança da API.
- **Spring Web e WebFlux**: Para criação de endpoints RESTful e suporte a comunicação reativa.
- **Spring Mail**: Para enviar e-mails a partir da aplicação.
- **JWT (JSON Web Token)**: Para autenticação e autorização, com suporte a tokens JWT.
- **QRCode e Código de Barras**: Usados para gerar códigos QR e de barras, utilizando as bibliotecas ZXing e Barcode4J.

## Configuração do Ambiente

Para que você possa iniciar a aplicação e acessar os recursos via API Rest com Postman ou Insomnia, siga os passos abaixo:
* Define o valor das variáveis de ambiente no arquivo `application.properties`
```properties
criptografia.senha=${CRIPTOGRAFIA_SENHA:teste1234}
```
* Execute a aplicação para criar a tabelas via migrations.
  > Ao Executar a aplicação você poderá definir uma senha de login para sua empresa (tab_empresa), avaliar a classe `com.izatec.pay.App`  

* Realize um insert na tabela `tab_empresa` manualmente ou via migrations, exemplo:
```sql
INSERT INTO public.tab_empresa (cpf_cnpj, nome_fantasia, razao_social, email, whatsapp, senha) VALUES('84306987000167', 'Empresa Teste', 'Empresa Teste', 'email@gmail.com', 11912345678, 'gLOY+ofiySCVlzG3nZRYPg==');

```

## Cobranças e Pagamentos

Para registrar cobranças e pagamentos será necessário gerar uma configuração vinculada ou não a algum player de pagamento como Lytex, ModoBank basta você inserir um registro na tabela `tab_configuracao` com os dados de integração, exemplo:
```sql
-- NÃO EXECUTAR

INSERT INTO public.tab_configuracao (id, empresa_id, certificado_nome, certificado_senha, custo_integracao, intermediador_sigla, intermediador_id, intermediador_senha, intermediador_chave_pix) VALUES('CPF_CNPJ_8_DIGITOS+0000001', 1, 'NOME_CERTIFICADO_SEM.pfx', 'SENHA_CERTIFICADO', 0.0, 'ONZ_OU_LYTEX', 'CLIENT_ID_INTEGRADORA', 'CLIENT_SECRET_INTEGRADORA', 'CHAVE_PIX_INTEGRADORA');
```` 
> **Observação:** O campo `id` precisa seguir o padrão `CPF_CNPJ_8_DIGITOS+0000001`, onde os 8 primeiros dígitos são o CNPJ da empresa e o restante é um número sequencial ou identificação externa para identificar a configuração.

Conta Teste
```sql
INSERT INTO public.tab_configuracao (id, empresa_id, certificado_nome, certificado_senha, custo_integracao, intermediador_sigla, intermediador_id, intermediador_senha, intermediador_chave_pix) VALUES('843069870000001', 1, '84306987000167.pfx', 'abc123', 0.0, 'ONZ', 'abc123', 'abc123', 'abc123');
```` 

* Consulta os recursos na página do Swagger: `http://localhost:8080/swagger-ui/index.html`
  > Você pode acessar a documentação da API através do Swagger, que fornece uma interface interativa para testar os endpoints disponíveis. Acesse `http://localhost:8080/swagger-ui/index.html após iniciar a aplicação.

![Swagger](/src/main/resources/swagger.png)

## Exemplos

Consulta a collection via Postman `izapay-opensource.postman_collection.json` 

Login:

* Usuario: 84306987000167
* Senha: 8430
