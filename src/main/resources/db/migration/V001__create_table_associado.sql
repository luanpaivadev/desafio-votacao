CREATE TABLE public.associado (
	id bigserial NOT NULL,
	cpf varchar(255) NOT NULL,
	nome varchar(255) NOT NULL,
	CONSTRAINT associado_pkey PRIMARY KEY (id),
	CONSTRAINT cpf_un UNIQUE (cpf)
);