drop table public.tab_cobranca_ativacao;
drop table public.tab_atendimento  ;
drop table public.tab_notificacao ;
drop table public.tab_pagamento;
drop table public.tab_cobranca;
drop table public.tab_despesa;
drop table public.tab_previsao ;
drop table public.tab_configuracao ;
drop table public.tab_cadastro;
drop table public.tab_cep;
drop table public.tab_anexo;
drop table public.tab_empresa;


delete from public.flyway_schema_history;

DROP EXTENSION IF EXISTS unaccent;
DROP FUNCTION IF EXISTS normalize_text;
