CREATE TABLE public.tab_empresa (
	id                              serial4             NOT NULL,
	cpf_cnpj                        varchar(20)         NOT NULL,
	nome_fantasia                   varchar(100)        NOT NULL,
    razao_social                    varchar(100)        NOT NULL,
    email                           varchar(100)        NOT NULL,
	whatsapp                        int8                NOT NULL,
	senha                           varchar(150)            NULL,
	CONSTRAINT pk_empresa           PRIMARY KEY (id)
);

CREATE TABLE public.tab_configuracao (
	id                              varchar(20)         NOT NULL,
	empresa_id                      int4                NOT NULL,
	certificado_nome                varchar(50)         NOT NULL,
	certificado_senha               varchar(50)         NOT NULL,
	custo_integracao                numeric(5,2)        NOT NULL,
	intermediador_sigla             varchar(10)         NOT NULL,
    intermediador_id                varchar(100)        NOT NULL,
    intermediador_senha             varchar(256)        NOT NULL,
	intermediador_chave_pix         varchar(50)             NULL,

	CONSTRAINT pk_configuracao      PRIMARY KEY (id),
	CONSTRAINT fk_configuracao      FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id)
);

CREATE TABLE public.tab_cadastro (
	id                              serial4             NOT NULL,
	documento                       varchar(20)         NOT NULL,
	nome_completo                   varchar(50)         NOT NULL,
	dt_nascto                       date                    NULL,
	email                           varchar(100)            NULL,
	whatsapp                        varchar(15)             NULL,
	notif_email                     bool                NOT NULL,
    notif_whatsapp                  bool                NOT NULL,
	end_cep                         varchar(10)             NULL,
    end_logradouro                  varchar(200)            NULL,
    end_numero                      varchar(80)             NULL,
	avalista_id                     int4                    NULL,
    token                           varchar(6)              NULL,
    empresa_id                      int4                NOT NULL,


	CONSTRAINT tab_cadastro_pkey    PRIMARY KEY (id),
    CONSTRAINT fk_cad_empresa       FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id)
);


CREATE TABLE public.tab_cobranca (
	id                              serial4             NOT NULL,
	cod_externo                     varchar(10)         NOT NULL,
	status                          varchar(15)         NOT NULL,
	geracao_dia                     date                NOT NULL,
    geracao_hora                    time(6)             NOT NULL,
	config_cnpj                     varchar(20)             NULL,
    config_nome_fantasia            varchar(100)            NULL,
    config_identificacao            varchar(20)             NULL,
	titulo                          varchar(60)         NOT NULL,
	scd_id                          int4                    NULL,
    scd_documento                   varchar(20)             NULL,
    scd_nome_completo               varchar(100)            NULL,
    vl_cobrado                      numeric(9,2)        NOT NULL,
    vl_cobranca                     numeric(9,2)        NOT NULL,
	ngcc_modelo                     varchar(15)         NOT NULL,
	ngcc_dia_vencto                 int4                NOT NULL,
	ngcc_nr_prox_parcela            int4                NOT NULL,
	ngcc_dt_prox_vencto             date                NOT NULL,
	ngcc_recorrencia                varchar(15)             NULL,
	qtd_parcelas                    int4                NOT NULL,
	descricao                       varchar(100)            NULL,
    observacao                      varchar(200)            NULL,
    endereco                        varchar(150)            NULL,
    vigencia_dia                    date                    NULL,
    vigencia_hora                   time(6)                 NULL,
    empresa_id                      int4                NOT NULL,

	CONSTRAINT pk_cobranca          PRIMARY KEY (id),
	CONSTRAINT fk_cob_empresa       FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id),
    CONSTRAINT fk_cob_cadastro      FOREIGN KEY (scd_id) REFERENCES public.tab_cadastro(id)
);

CREATE TABLE public.tab_pagamento (
	id                              serial4             NOT NULL,
	cod_externo                     varchar(10)         NOT NULL,
	status                          varchar(15)         NOT NULL,
	cod_identificacao               varchar(35)         NOT NULL,
	geracao_dia                     date                NOT NULL,
    geracao_hora                    time(6)             NOT NULL,
    prev_pag_dia                    date                NOT NULL,
    prev_pag_hora                   time(6)             NOT NULL,
    vencto_dia                      date                NOT NULL,
    vencto_hora                     time(6)             NOT NULL,
    vl_original                     numeric(9,2)        NOT NULL,
    vl_pago                         numeric(9,2)        NOT NULL,
    mensagem                        varchar(150)        NOT NULL,
    parcela                         int4                    NULL,
    scd_id                          int4                    NULL,
    scd_documento                   varchar(20)             NULL,
    scd_nome_completo               varchar(100)            NULL,
    int_num_tentativas              int4                    NULL,
    int_dh_integracao               timestamp(6)            NULL,
	int_conteudo                    varchar(255)            NULL,
	int_link                        varchar(255)            NULL,
    int_intermediador               varchar(10)             NULL,
    int_erro                        bool                NOT NULL,
	cpsc_comprovante                varchar(50)             NULL,
	cpsc_dia                        date                    NULL,
	cpsc_hora                       time(6)                 NULL,
	cpsc_observacao                 varchar(150)            NULL,
	cpsc_custo                      numeric(5,2)            NULL,
	config_cnpj                     varchar(20)             NULL,
    config_nome_fantasia            varchar(100)            NULL,
    config_identificacao            varchar(20)             NULL,
	cobranca_id                     int4                    NULL,
	observacao                      varchar(200)            NULL,
	empresa_id                      int4                NOT NULL,

	CONSTRAINT pk_pagamento         PRIMARY KEY (id),
    CONSTRAINT fk_pagto_empresa     FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id),
    CONSTRAINT fk_pagto_cobranca    FOREIGN KEY (cobranca_id) REFERENCES public.tab_cobranca(id),
    CONSTRAINT fk_pagto_cadastro    FOREIGN KEY (scd_id) REFERENCES public.tab_cadastro(id)
);

CREATE TABLE public.tab_previsao (
    id                              serial4             NOT NULL,
    cod_externo                     varchar(255)        NOT NULL,
    geracao_dia                     date                NOT NULL,
    geracao_hora                    time(6)             NOT NULL,
    status                          varchar(20)         NOT NULL,
    titulo                          varchar(60)         NOT NULL,
    vl_despesa                      numeric(9,2)        NOT NULL,
    qtd_parcelas                    int4                NOT NULL,
    vl_pago                         numeric(9,2)        NOT NULL,
    descricao                       varchar(100)        NOT NULL,
    aplc_grupo                      varchar(30)             NULL,
    aplc_categoria                  varchar(30)             NULL,
    fav_id                          int4                    NULL,
    fav_documento                   varchar(20)             NULL,
    fav_nome_completo               varchar(100)            NULL,
    ngcc_dia_vencto                 int4                NOT NULL,
    ngcc_modelo                     varchar(20)         NOT NULL,
    ngcc_nr_prox_parcela            int4                NOT NULL,
    ngcc_dt_prox_vencto             date                NOT NULL,
    ngcc_recorrencia                varchar(20)         NOT NULL,
    observacao                      varchar(200)            NULL,
    empresa_id                      int4                NOT NULL,
    CONSTRAINT pk_previsao          PRIMARY KEY (id),
    CONSTRAINT fk_prev_empresa      FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id),
    CONSTRAINT fk_prev_cadastro     FOREIGN KEY (fav_id) REFERENCES public.tab_cadastro(id)

);

CREATE TABLE public.tab_despesa (
    id                              serial4             NOT NULL,
    cod_externo                     varchar(10)         NOT NULL,
    parcela                         int4                NOT NULL,
    geracao_dia                     date                NOT NULL,
    geracao_hora                    time(6)             NOT NULL,
    vencto_dia                      date                NOT NULL,
    vencto_hora                     time(6)             NOT NULL,
    vl_original                     numeric(9,2)        NOT NULL,
    vl_pago                         numeric(9,2)        NOT NULL,
    mensagem                        varchar(150)        NOT NULL,
    aplc_grupo                      varchar(30)             NULL,
    aplc_categoria                  varchar(30)             NULL,
    status                          varchar(10)         NOT NULL,
    fav_id                          int4                    NULL,
    fav_documento                   varchar(20)             NULL,
    fav_nome_completo               varchar(100)            NULL,
    cpsc_comprovante                varchar(50)             NULL,
    cpsc_custo                      numeric(5,2)            NULL,
    cpsc_dia                        date                    NULL,
    cpsc_hora                       time(6)                 NULL,
    cpsc_observacao                 varchar(100)            NULL,
    previsao_id                     int4                    NULL,
    empresa_id                      int4                    NULL,

    CONSTRAINT pk_despesa           PRIMARY KEY (id),
    CONSTRAINT fk_desp_empresa      FOREIGN KEY (empresa_id) REFERENCES public.tab_empresa(id),
    CONSTRAINT fk_desp_previsao     FOREIGN KEY (previsao_id) REFERENCES public.tab_despesa(id),
    CONSTRAINT fk_desp_cadastro     FOREIGN KEY (fav_id) REFERENCES public.tab_cadastro(id)
);

CREATE TABLE public.tab_notificacao (
    id                              serial4             NOT NULL,
    data_dia                        date                NOT NULL,
    data_hora                       time(6)             NOT NULL,
    descricao                       varchar(150)        NOT NULL,
    remetente                       varchar(100)        NOT NULL,
    destinatario                    varchar(100)        NOT NULL,
    origem                          char(1)             NOT NULL,
    vl_custo                        numeric(5,2)        NOT NULL,
    env_entregue                    bool                NOT NULL,
    env_dia                         date                    NULL,
    env_hora                        time(6)                 NULL,
    env_nr_protocolo                varchar(100)            NULL,
    env_resposta                    varchar(150)            NULL,
    CONSTRAINT pk_notificacao       PRIMARY KEY (id)
);

-- Comentando a tabela tab_empresa
COMMENT ON TABLE public.tab_empresa IS 'Armazena informações das empresas.';

-- Comentando as colunas da tabela tab_empresa
COMMENT ON COLUMN public.tab_empresa.id IS 'Identificador único da empresa.';
COMMENT ON COLUMN public.tab_empresa.cpf_cnpj IS 'CPF ou CNPJ da empresa.';
COMMENT ON COLUMN public.tab_empresa.nome_fantasia IS 'Nome fantasia da empresa.';
COMMENT ON COLUMN public.tab_empresa.razao_social IS 'Razão social da empresa.';
COMMENT ON COLUMN public.tab_empresa.email IS 'Email de contato da empresa.';
COMMENT ON COLUMN public.tab_empresa.whatsapp IS 'Número de WhatsApp da empresa.';
COMMENT ON COLUMN public.tab_empresa.senha IS 'Senha para autenticação da empresa.';

-- Comentando a tabela tab_configuracao
COMMENT ON TABLE public.tab_configuracao IS 'Armazena configurações relacionadas a cada empresa.';

-- Comentando as colunas da tabela tab_configuracao
COMMENT ON COLUMN public.tab_configuracao.id IS 'Identificador único da configuração.';
COMMENT ON COLUMN public.tab_configuracao.empresa_id IS 'Identificador da empresa.';
COMMENT ON COLUMN public.tab_configuracao.certificado_nome IS 'Nome do certificado digital (.pfx).';
COMMENT ON COLUMN public.tab_configuracao.certificado_senha IS 'Senha do certificado digital.';
COMMENT ON COLUMN public.tab_configuracao.custo_integracao IS 'Custo de integração.';
COMMENT ON COLUMN public.tab_configuracao.intermediador_sigla IS 'Sigla do intermediador:ONZ, LYTEX e etc.';
COMMENT ON COLUMN public.tab_configuracao.intermediador_id IS 'Identificador do intermediador: clienteId.';
COMMENT ON COLUMN public.tab_configuracao.intermediador_senha IS 'Senha do intermediador: secretId.';
COMMENT ON COLUMN public.tab_configuracao.intermediador_chave_pix IS 'Chave PIX do intermediador (opcional).';

-- Comentando a tabela tab_cadastro
COMMENT ON TABLE public.tab_cadastro IS 'Armazena informações sobre o cadastro dos clientes e fornecedores da empresa.';

-- Comentando as colunas da tabela tab_cadastro
COMMENT ON COLUMN public.tab_cadastro.id IS 'Identificador único do cadastro.';
COMMENT ON COLUMN public.tab_cadastro.documento IS 'Documento como CPF, CNPJ, RG ou valor aleatorio unico.';
COMMENT ON COLUMN public.tab_cadastro.nome_completo IS 'Nome completo.';
COMMENT ON COLUMN public.tab_cadastro.dt_nascto IS 'Data de nascimento.';
COMMENT ON COLUMN public.tab_cadastro.email IS 'Email.';
COMMENT ON COLUMN public.tab_cadastro.whatsapp IS 'Número de WhatsApp.';
COMMENT ON COLUMN public.tab_cadastro.notif_email IS 'Indica se o cliente recebe notificações por email.';
COMMENT ON COLUMN public.tab_cadastro.notif_whatsapp IS 'Indica se o cliente recebe notificações por WhatsApp.';
COMMENT ON COLUMN public.tab_cadastro.end_cep IS 'CEP do endereço.';
COMMENT ON COLUMN public.tab_cadastro.end_logradouro IS 'Logradouro do endereço.';
COMMENT ON COLUMN public.tab_cadastro.end_numero IS 'Número do endereço.';
COMMENT ON COLUMN public.tab_cadastro.avalista_id IS 'Identificador do avalista, se houver.';
COMMENT ON COLUMN public.tab_cadastro.empresa_id IS 'Identificador da empresa.';

-- Comentando a tabela tab_cobranca
COMMENT ON TABLE public.tab_cobranca IS 'Armazena informações de cobranças realizadas pela empresa.';

-- Comentando as colunas da tabela tab_cobranca
COMMENT ON COLUMN public.tab_cobranca.id IS 'Identificador único da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.cod_externo IS 'Código externo da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.status IS 'Status da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.geracao_dia IS 'Data de geração da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.geracao_hora IS 'Hora de geração da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.config_cnpj IS 'CNPJ configurado (se houver).';
COMMENT ON COLUMN public.tab_cobranca.config_nome_fantasia IS 'Nome fantasia configurado (se houver).';
COMMENT ON COLUMN public.tab_cobranca.config_identificacao IS 'Identificação configurada (se houver).';
COMMENT ON COLUMN public.tab_cobranca.titulo IS 'Título da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.scd_id IS 'ID do cadastro associado à cobrança.';
COMMENT ON COLUMN public.tab_cobranca.scd_documento IS 'Documento como CPF, CNPJ, RG ou valor aleatorio unico.';
COMMENT ON COLUMN public.tab_cobranca.scd_nome_completo IS 'Nome completo associado à cobrança.';
COMMENT ON COLUMN public.tab_cobranca.vl_cobrado IS 'Valor cobrado na cobrança.';
COMMENT ON COLUMN public.tab_cobranca.vl_cobranca IS 'Valor total da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.ngcc_modelo IS 'Modelo de cobrança no sistema NGCC.';
COMMENT ON COLUMN public.tab_cobranca.ngcc_dia_vencto IS 'Dia de vencimento da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.ngcc_nr_prox_parcela IS 'Número da próxima parcela.';
COMMENT ON COLUMN public.tab_cobranca.ngcc_dt_prox_vencto IS 'Data de vencimento da próxima parcela.';
COMMENT ON COLUMN public.tab_cobranca.ngcc_recorrencia IS 'Recorrência de cobrança, se houver.';
COMMENT ON COLUMN public.tab_cobranca.qtd_parcelas IS 'Quantidade de parcelas.';
COMMENT ON COLUMN public.tab_cobranca.descricao IS 'Descrição da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.observacao IS 'Observação adicional sobre a cobrança.';
COMMENT ON COLUMN public.tab_cobranca.endereco IS 'Endereço associado à cobrança.';
COMMENT ON COLUMN public.tab_cobranca.vigencia_dia IS 'Data de vigência da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.vigencia_hora IS 'Hora de vigência da cobrança.';
COMMENT ON COLUMN public.tab_cobranca.empresa_id IS 'Identificador da empresa associada à cobrança.';

-- Comentando a tabela tab_pagamento
 COMMENT ON TABLE public.tab_pagamento IS 'Armazena informações sobre os pagamentos realizados pela empresa.';

-- Comentando as colunas da tabela tab_pagamento
 COMMENT ON COLUMN public.tab_pagamento.id IS 'Identificador único do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.cod_externo IS 'Código externo do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.status IS 'Status do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.cod_identificacao IS 'Código de identificação do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.geracao_dia IS 'Data de geração do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.geracao_hora IS 'Hora de geração do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.prev_pag_dia IS 'Data prevista para o pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.prev_pag_hora IS 'Hora prevista para o pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.vencto_dia IS 'Data de vencimento do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.vencto_hora IS 'Hora de vencimento do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.vl_original IS 'Valor original do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.vl_pago IS 'Valor efetivamente pago.';
 COMMENT ON COLUMN public.tab_pagamento.mensagem IS 'Mensagem associada ao pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.parcela IS 'Número da parcela, se houver.';
 COMMENT ON COLUMN public.tab_pagamento.scd_id IS 'ID do cadastro associado ao pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.scd_documento IS 'Documento como CPF, CNPJ, RG ou valor aleatorio unico.';
 COMMENT ON COLUMN public.tab_pagamento.scd_nome_completo IS 'Nome completo associado ao pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.int_num_tentativas IS 'Número de tentativas de pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.int_dh_integracao IS 'Data e hora da integração do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.int_conteudo IS 'Conteúdo do pagamento, se necessário.';
 COMMENT ON COLUMN public.tab_pagamento.int_link IS 'Link associado ao pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.int_intermediador IS 'Intermediador do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.int_erro IS 'Indica se houve erro no pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.cpsc_comprovante IS 'Comprovante do pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.cpsc_dia IS 'Data do comprovante.';
 COMMENT ON COLUMN public.tab_pagamento.cpsc_hora IS 'Hora do comprovante.';
 COMMENT ON COLUMN public.tab_pagamento.cpsc_observacao IS 'Observação sobre o comprovante.';
 COMMENT ON COLUMN public.tab_pagamento.cpsc_custo IS 'Custo do comprovante.';
 COMMENT ON COLUMN public.tab_pagamento.config_cnpj IS 'CNPJ configurado (se houver).';
 COMMENT ON COLUMN public.tab_pagamento.config_nome_fantasia IS 'Nome fantasia configurado (se houver).';
 COMMENT ON COLUMN public.tab_pagamento.config_identificacao IS 'Identificação configurada (se houver).';
 COMMENT ON COLUMN public.tab_pagamento.cobranca_id IS 'ID da cobrança associada.';
 COMMENT ON COLUMN public.tab_pagamento.observacao IS 'Observação adicional sobre o pagamento.';
 COMMENT ON COLUMN public.tab_pagamento.empresa_id IS 'Identificador da empresa associada ao pagamento.';

-- Comentando a tabela tab_previsao
COMMENT ON TABLE public.tab_previsao IS 'Armazena informações sobre previsões financeiras da empresa.';

-- Comentando as colunas da tabela tab_previsao
COMMENT ON COLUMN public.tab_previsao.id IS 'Identificador único da previsão.';
COMMENT ON COLUMN public.tab_previsao.cod_externo IS 'Código externo da previsão.';
COMMENT ON COLUMN public.tab_previsao.geracao_dia IS 'Data de geração da previsão.';
COMMENT ON COLUMN public.tab_previsao.geracao_hora IS 'Hora de geração da previsão.';
COMMENT ON COLUMN public.tab_previsao.status IS 'Status da previsão.';
COMMENT ON COLUMN public.tab_previsao.titulo IS 'Título da previsão.';
COMMENT ON COLUMN public.tab_previsao.vl_despesa IS 'Valor da despesa prevista.';
COMMENT ON COLUMN public.tab_previsao.qtd_parcelas IS 'Quantidade de parcelas para a previsão.';
COMMENT ON COLUMN public.tab_previsao.vl_pago IS 'Valor pago até o momento.';
COMMENT ON COLUMN public.tab_previsao.descricao IS 'Descrição detalhada da previsão.';
COMMENT ON COLUMN public.tab_previsao.aplc_grupo IS 'Grupo da aplicação (se houver).';
COMMENT ON COLUMN public.tab_previsao.aplc_categoria IS 'Categoria da aplicação (se houver).';
COMMENT ON COLUMN public.tab_previsao.fav_id IS 'ID do favorecido relacionado à previsão.';
COMMENT ON COLUMN public.tab_previsao.fav_documento IS 'Documento como CPF, CNPJ, RG ou valor aleatorio unico.';
COMMENT ON COLUMN public.tab_previsao.fav_nome_completo IS 'Nome completo do favorecido.';
COMMENT ON COLUMN public.tab_previsao.ngcc_dia_vencto IS 'Dia de vencimento da previsão.';
COMMENT ON COLUMN public.tab_previsao.ngcc_modelo IS 'Modelo de cobrança do sistema NGCC.';
COMMENT ON COLUMN public.tab_previsao.ngcc_nr_prox_parcela IS 'Número da próxima parcela da previsão.';
COMMENT ON COLUMN public.tab_previsao.ngcc_dt_prox_vencto IS 'Data de vencimento da próxima parcela da previsão.';
COMMENT ON COLUMN public.tab_previsao.ngcc_recorrencia IS 'Recorrência de cobrança da previsão.';
COMMENT ON COLUMN public.tab_previsao.observacao IS 'Observação adicional sobre a previsão.';
COMMENT ON COLUMN public.tab_previsao.empresa_id IS 'Identificador da empresa associada à previsão.';

-- Comentando a tabela tab_despesa
COMMENT ON TABLE public.tab_despesa IS 'Armazena informações sobre as despesas realizadas pela empresa.';

-- Comentando as colunas da tabela tab_despesa
COMMENT ON COLUMN public.tab_despesa.id IS 'Identificador único da despesa.';
COMMENT ON COLUMN public.tab_despesa.cod_externo IS 'Código externo da despesa.';
COMMENT ON COLUMN public.tab_despesa.parcela IS 'Número da parcela da despesa.';
COMMENT ON COLUMN public.tab_despesa.geracao_dia IS 'Data de geração da despesa.';
COMMENT ON COLUMN public.tab_despesa.geracao_hora IS 'Hora de geração da despesa.';
COMMENT ON COLUMN public.tab_despesa.vencto_dia IS 'Data de vencimento da despesa.';
COMMENT ON COLUMN public.tab_despesa.vencto_hora IS 'Hora de vencimento da despesa.';
COMMENT ON COLUMN public.tab_despesa.vl_original IS 'Valor original da despesa.';
COMMENT ON COLUMN public.tab_despesa.vl_pago IS 'Valor pago pela despesa.';
COMMENT ON COLUMN public.tab_despesa.mensagem IS 'Mensagem associada à despesa.';
COMMENT ON COLUMN public.tab_despesa.aplc_grupo IS 'Grupo de aplicação da despesa.';
COMMENT ON COLUMN public.tab_despesa.aplc_categoria IS 'Categoria da despesa.';
COMMENT ON COLUMN public.tab_despesa.status IS 'Status da despesa.';
COMMENT ON COLUMN public.tab_despesa.fav_id IS 'ID do favorecido associado à despesa.';
COMMENT ON COLUMN public.tab_despesa.fav_documento IS 'Documento como CPF, CNPJ, RG ou valor aleatorio unico.';
COMMENT ON COLUMN public.tab_despesa.fav_nome_completo IS 'Nome completo do favorecido associado à despesa.';
COMMENT ON COLUMN public.tab_despesa.cpsc_comprovante IS 'Comprovante de pagamento da despesa.';
COMMENT ON COLUMN public.tab_despesa.cpsc_custo IS 'Custo associado ao comprovante da despesa.';
COMMENT ON COLUMN public.tab_despesa.cpsc_dia IS 'Dia do comprovante de pagamento.';
COMMENT ON COLUMN public.tab_despesa.cpsc_hora IS 'Hora do comprovante de pagamento.';
COMMENT ON COLUMN public.tab_despesa.cpsc_observacao IS 'Observação sobre o comprovante de pagamento.';
COMMENT ON COLUMN public.tab_despesa.previsao_id IS 'ID da previsão associada à despesa.';
COMMENT ON COLUMN public.tab_despesa.empresa_id IS 'Identificador da empresa associada à despesa.';

-- Comentando a tabela tab_notificacao
COMMENT ON TABLE public.tab_notificacao IS 'Armazena notificações enviadas pela empresa.';

-- Comentando as colunas da tabela tab_notificacao
COMMENT ON COLUMN public.tab_notificacao.id IS 'Identificador único da notificação.';
COMMENT ON COLUMN public.tab_notificacao.data_dia IS 'Data em que a notificação foi gerada.';
COMMENT ON COLUMN public.tab_notificacao.data_hora IS 'Hora em que a notificação foi gerada.';
COMMENT ON COLUMN public.tab_notificacao.descricao IS 'Descrição da notificação.';
COMMENT ON COLUMN public.tab_notificacao.remetente IS 'Nome do remetente da notificação.';
COMMENT ON COLUMN public.tab_notificacao.destinatario IS 'Destinatário da notificação.';
COMMENT ON COLUMN public.tab_notificacao.origem IS 'Origem da notificação (ex: "E", "S").';
COMMENT ON COLUMN public.tab_notificacao.vl_custo IS 'Custo associado ao envio da notificação.';
COMMENT ON COLUMN public.tab_notificacao.env_entregue IS 'Indica se a notificação foi entregue.';
COMMENT ON COLUMN public.tab_notificacao.env_dia IS 'Data de entrega da notificação.';
COMMENT ON COLUMN public.tab_notificacao.env_hora IS 'Hora de entrega da notificação.';
COMMENT ON COLUMN public.tab_notificacao.env_nr_protocolo IS 'Número de protocolo da entrega da notificação.';
COMMENT ON COLUMN public.tab_notificacao.env_resposta IS 'Resposta recebida do destinatário (se houver).';
