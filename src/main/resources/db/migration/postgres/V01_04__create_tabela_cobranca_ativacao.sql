CREATE TABLE public.tab_cobranca_ativacao (
	id                              serial4             NOT NULL,
	id_cobranca                     int8                NOT NULL,
	ip                              varchar(40)         NOT NULL,
    data_hora                       timestamp           NOT NULL,
	CONSTRAINT id                   PRIMARY KEY (id)
);