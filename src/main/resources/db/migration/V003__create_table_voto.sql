CREATE TABLE public.voto (
	id bigserial NOT NULL,
	data_hora_voto timestamp(6) NULL,
	voto varchar(255) NULL,
	associado_id int8 NULL,
	pauta_id int8 NULL,
	CONSTRAINT voto_pkey PRIMARY KEY (id),
	CONSTRAINT voto_un UNIQUE (associado_id, pauta_id),
	CONSTRAINT associado_id_fk FOREIGN KEY (associado_id) REFERENCES public.associado(id),
	CONSTRAINT pauta_id_fk FOREIGN KEY (pauta_id) REFERENCES public.pauta(id)
);