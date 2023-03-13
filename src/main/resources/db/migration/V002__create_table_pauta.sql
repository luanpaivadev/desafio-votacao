CREATE TABLE public.pauta (
	id bigserial NOT NULL,
	data_hora_fim timestamp(6) NULL,
	data_hora_inicio timestamp(6) NULL,
	descricao varchar(255) NOT NULL,
	situacao varchar(255) NOT NULL,
	CONSTRAINT pauta_pkey PRIMARY KEY (id)
);