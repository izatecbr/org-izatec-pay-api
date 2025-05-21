
# Iza Pay

## Sistema de Gestão de Pagamentos

Este sistema foi desenvolvido para simplificar a gestão de pagamentos, oferecendo uma solução eficiente para controlar as finanças da empresa, registrar cobranças e pagamentos, gerenciar previsões de despesas e notificações, e integrar com serviços externos como plataformas de pagamento e envio de mensagens.

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

## Configuração do Ambiente

Para que você possa iniciar a aplicação e acessar os recursos via API Rest com Postman ou Insomnia, siga os passos abaixo:

* Define o valor das variáveis de ambiente no arquivo `application.properties`, Consulte o seu ambiente de produção para definir os valores corretos.

```properties
criptografia.senha=${CRIPTOGRAFIA_SENHA:teste1234}
```
* Execute a aplicação para criar a tabelas via migrations.
  > Ao Executar a aplicação você poderá definir uma senha de login para sua empresa (tab_empresa), avaliar a classe `com.izatec.pay.App`  

NOTA: Use os 04 primeiros dígitos do CPF/CNPJ da empresa para criar a senha de login, exemplo: `4743` (47435652000112) ou `1234` (12345678900).

* Realize um insert na tabela `tab_empresa` manualmente ou via migrations, exemplo:
```sql
INSERT INTO public.tab_empresa (cpf_cnpj, nome_fantasia, razao_social, email, whatsapp, senha) VALUES('12345678900', 'Gleyson Sampaio', 'Gleyson Sampaio', 'gleyson@iza.tec.br', 11958940362, 'H7qOuXXMAd0ATzzFvuTCSw==');

```

## Cobranças e Pagamentos

Para registrar cobranças e pagamentos será necessário gerar uma configuração vinculada ou não a algum player de pagamento como Lytex, ModoBank basta você inserir um registro na tabela `tab_configuracao` com os dados de integração, exemplo:
```sql
INSERT INTO public.tab_configuracao (id, empresa_id, certificado_nome, certificado_senha, custo_integracao, intermediador_sigla, intermediador_id, intermediador_senha, intermediador_chave_pix) VALUES('CPF_CNPJ_8_DIGITOS+0000001', 1, 'NOME_CERTIFICADO_SEM.pfx', 'SENHA_CERTIFICADO', 0.0, 'ONZ_OU_LYTEX', 'CLIENT_ID_INTEGRADORA', 'CLIENT_SECRET_INTEGRADORA', 'CHAVE_PIX_INTEGRADORA');
```` 
> **Observação:** O campo `id` precisa seguir o padrão `CPF_CNPJ_8_DIGITOS+0000001`, onde os 8 primeiros dígitos são o CNPJ da empresa e o restante é um número sequencial ou identificação externa para identificar a configuração.


## Integração com Gateway de Pagamento

Algumas integrações como a do ModoBank necessita de uma senha de 8 digitos para autorizar a compensação via webhook, para isso você pode gerar uma senha de 8 digitos que correspondem ao resultado do identificador numerico gerado na tabela `tab_configuracao`, exemplo:

* CNPJ Empresa: 47435652000112 
* CNPJ Raíz: 47435652
* CNPJ Configuração: 4743565200000001

Via classe `com.izatec.pay.App` realize a criptografia do *CNPJ Configuração:* `474356520000001`, exemplo:
```java
@Component
public class App implements ApplicationRunner {
  @Autowired
  private AcessoService acessoService;
  @Override
  public void run(ApplicationArguments args) throws Exception {
    System.out.println(acessoService.criptografar("474356520000001"));
  }
}
```
```shell
Expectativa de saída: fLSyE7Op/cV3afofT1kYtw==
``` 

* Consulta os recursos na página do Swagger: `http://localhost:8080/swagger-ui/index.html`
  > Você pode acessar a documentação da API através do Swagger, que fornece uma interface interativa para testar os endpoints disponíveis. Acesse `http://localhost:8080/swagger-ui/index.html após iniciar a aplicação.


RELEASE

![Swagger](/dev/swagger.png)
