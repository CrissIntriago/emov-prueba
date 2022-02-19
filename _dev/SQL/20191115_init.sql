-- INIT SQL DATABASE --

CREATE TABLE public.catalogo
(
    id bigserial,
    codigo character varying(250) COLLATE pg_catalog."default",
    nombre character varying(250) COLLATE pg_catalog."default",
    descripcion text COLLATE pg_catalog."default",
    CONSTRAINT catalogo_pkey PRIMARY KEY (id),
	CONSTRAINT codigo_unique unique (codigo)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.catalogo
    OWNER to postgres;
	

CREATE TABLE public.catalogo_item
(
    id bigserial,
    catalogo bigint,
    orden smallint,
    texto character varying(250) COLLATE pg_catalog."default",
    descripcion character varying COLLATE pg_catalog."default",
    default_value boolean DEFAULT true,
    codigo character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT catalogo_item_pkey PRIMARY KEY (id),
    CONSTRAINT fk_catalogo FOREIGN KEY (catalogo)
        REFERENCES public.catalogo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.catalogo_item
    OWNER to postgres;	
	

CREATE TABLE public.nivel
(
    id bigserial,
    tipo bigint NOT NULL,
    nombre character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default",
    longitud smallint NOT NULL,
    orden smallint NOT NULL,
    CONSTRAINT pk_nivel PRIMARY KEY (id),
    CONSTRAINT fk_catalogo_item_nivel FOREIGN KEY (tipo)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.nivel
    OWNER to postgres;	
	

CREATE TABLE public.master_catalogo
(
    id bigserial,
    anio smallint NOT NULL,
    tipo bigint not null,
    nombre character varying(100) COLLATE pg_catalog."default",
    descripcion character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT master_catalogo_pkey PRIMARY KEY (id),
	CONSTRAINT master_catalogo_unique unique (anio, tipo),
    CONSTRAINT master_ctgo_ctgo_item FOREIGN KEY (tipo)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.master_catalogo
    OWNER to postgres;	
	
		
CREATE TABLE public.clasificacion_cuenta
(
    id bigserial,
    tipo_cuenta bigint NOT NULL,
    codigo character varying(15) COLLATE pg_catalog."default" NOT NULL,
    descripcion character varying(200) COLLATE pg_catalog."default" NOT NULL,
    orden smallint NOT NULL,
    CONSTRAINT clasificacion_cuenta_pkey PRIMARY KEY (id),
    CONSTRAINT clasificacion_cuenta_tipo_fkey FOREIGN KEY (tipo_cuenta)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.clasificacion_cuenta
    OWNER to postgres;	
	

CREATE TABLE public.catalogo_presupuesto
(
    id bigserial,
    padre bigint,
    titulo smallint NOT NULL,
    naturaleza smallint,
    sub_grupo smallint,
    rubro smallint,
    codigo character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default" NOT NULL,
    nivel bigint NOT NULL,
    flujo_ingreso boolean default true,
    cuenta_movimiento boolean NOT NULL DEFAULT true,
    clasificacion bigint NOT NULL,
    fecha_vigencia date,
    fecha_caducidad date,
    estado boolean NOT NULL DEFAULT true,
    anio smallint,
    CONSTRAINT pk_catalogo_presupuesto PRIMARY KEY (id),
    CONSTRAINT fk_catalogo_presupuesto_padre FOREIGN KEY (padre)
        REFERENCES public.catalogo_presupuesto (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_presupuesto_clasificacion FOREIGN KEY (clasificacion)
        REFERENCES public.clasificacion_cuenta (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_presupuesto_nivel FOREIGN KEY (nivel)
        REFERENCES public.nivel (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.catalogo_presupuesto
    OWNER to postgres;	

	

CREATE TABLE public.cliente
(
    id bigserial,
    identificacion character varying(20) COLLATE pg_catalog."default" NOT NULL,
	ruc character varying(3) COLLATE pg_catalog."default",
    tipo_identificacion bigint NOT NULL, 
	nombre character varying(250) COLLATE pg_catalog."default",
    apellido character varying(250) COLLATE pg_catalog."default" NOT NULL,
    direccion character varying(250) COLLATE pg_catalog."default",
    email character varying(500) COLLATE pg_catalog."default" ,
    telefono character varying(15) COLLATE pg_catalog."default",
    celular character varying(15) COLLATE pg_catalog."default",
    fecha_nacimiento date,
    genero bigint,
    discapacidad boolean DEFAULT false,
    num_conadis character varying(10) COLLATE pg_catalog."default",
    estado boolean NOT NULL DEFAULT true,
	identificacion_generada boolean DEFAULT false,
    CONSTRAINT cliente_pkey PRIMARY KEY (id),
    CONSTRAINT tipo_identificacion_cliente_fkey FOREIGN KEY (tipo_identificacion)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cliente
    OWNER to postgres;	
	

CREATE TABLE public.cuenta_contable
(
    id bigserial,
    padre bigint,
    titulo smallint NOT NULL,
    grupo smallint,
    sub_grupo smallint,
    cuenta_nivel_1 smallint,
    cuenta_nivel_2 smallint,
    cuenta_nivel_3 smallint,
    cuenta_nivel_4 smallint,
    codigo character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default" NOT NULL,
    nivel bigint NOT NULL,
    periodo smallint NOT NULL,
    estado boolean NOT NULL DEFAULT true,
    fecha_vigencia date,
    fecha_caducidad date,
    gobierno boolean NOT NULL DEFAULT false,
	devengado integer,
	cobrado integer,
    CONSTRAINT pk_cuenta_contable PRIMARY KEY (id),
    CONSTRAINT fk_cuenta_contable_nivel FOREIGN KEY (nivel)
        REFERENCES public.nivel (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_cuenta_contable_padre FOREIGN KEY (padre)
        REFERENCES public.cuenta_contable (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION	
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cuenta_contable
    OWNER to postgres;

COMMENT ON COLUMN public.cuenta_contable.gobierno
    IS 'Si la cuenta es de gobierno';




CREATE TABLE public.cuenta_pago
(
    id bigserial,
    tipo_cuenta bigint,
    numero_cuenta character varying(50) COLLATE pg_catalog."default",
    tipo_pago bigint,
    fecha_aprovacion date,
    fecha_desactivacion date,
    estado boolean DEFAULT true,
    restrictiva boolean DEFAULT true,
    tipo_banco bigint,
    transferencia boolean DEFAULT true,
    descripcion text COLLATE pg_catalog."default",
    beneficiario bigint NOT NULL,
    CONSTRAINT cuenta_pago_pkey PRIMARY KEY (id),
    CONSTRAINT cuenta_pago_beneficiario_fkey FOREIGN KEY (beneficiario)
        REFERENCES public.cliente (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_tipo_banco FOREIGN KEY (tipo_banco)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_tipo_cuenta FOREIGN KEY (tipo_cuenta)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_tipo_pago FOREIGN KEY (tipo_pago)
        REFERENCES public.catalogo_item (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.cuenta_pago
    OWNER to postgres;	
	
	
CREATE TABLE public.programacion_politica_publica
(
    id bigserial,
    actividad_operativa text COLLATE pg_catalog."default",
    meta_financiera smallint,
    descripcion_meta text COLLATE pg_catalog."default",
    codigo_unidad_administrativa_responsaable bigint,
    presupuesto_reformado numeric(15,2),
    CONSTRAINT programacion_politicas_publicas_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.programacion_politica_publica
    OWNER to postgres;	
	

CREATE TABLE public.numero_meta_logrado
(
    id bigserial,
    programacion_politica bigint,
    cuadrante_1 bigint,
    cuadrante_2 bigint,
    cuadrante_3 bigint,
    total_meta_lograda numeric(15,2),
	total_presupuesto_ejecutado numeric(15,2),
    resultado_eficacia numeric(12,2),
    CONSTRAINT pk_numero_meta_logrado PRIMARY KEY (id),
    CONSTRAINT fk_codigo_programacion_politica_publica FOREIGN KEY (programacion_politica)
        REFERENCES public.programacion_politica_publica (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.numero_meta_logrado
    OWNER to postgres;	
	

CREATE TABLE public.plan_desarrollo
(
    id bigserial,
    padre bigint,
    codigo character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descripcion character varying(200) COLLATE pg_catalog."default" NOT NULL,
    estado boolean NOT NULL,
    codigo_completo character varying(200) COLLATE pg_catalog."default",
    fecha_vigencia date,
    fecha_caducidad date,
    orden smallint NOT NULL,
    tipo character varying COLLATE pg_catalog."default",
    CONSTRAINT plan_desarrollo_pkey PRIMARY KEY (id),
    CONSTRAINT fk_padre_id FOREIGN KEY (padre)
        REFERENCES public.plan_desarrollo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.plan_desarrollo
    OWNER to postgres;	
	


CREATE TABLE public.plan_programatico
(
    id bigserial,
    padre bigint,
    codigo character varying(200) COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default" NOT NULL,
    periodo bigint NOT NULL,
    estado boolean NOT NULL,
    codigo_completo character varying(200) COLLATE pg_catalog."default" NOT NULL,
    fecha_vigente date,
    fecha_caducidad date,
    tipo character varying(200) COLLATE pg_catalog."default",
    nivel bigint,
    funcion smallint,
    programa smallint,
    subprograma smallint,
    CONSTRAINT pk_plan_presupuestario PRIMARY KEY (id),
    CONSTRAINT fk_nivel FOREIGN KEY (nivel)
        REFERENCES public.nivel (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_periodo FOREIGN KEY (periodo)
        REFERENCES public.master_catalogo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_plan_programatico_padre FOREIGN KEY (padre)
        REFERENCES public.plan_programatico (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.plan_programatico
    OWNER to postgres;	
	
	
CREATE TABLE public.regimen_laboral_contrato
(
    id bigserial,
    padre bigint,
    codigo character varying(250) COLLATE pg_catalog."default" NOT NULL,
    nombre character varying(300) COLLATE pg_catalog."default" NOT NULL,
    descripcion text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT regimen_laboral_contrato_pkey PRIMARY KEY (id),
    CONSTRAINT fk_padre FOREIGN KEY (padre)
        REFERENCES public.regimen_laboral_contrato (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.regimen_laboral_contrato
    OWNER to postgres;	
	

CREATE TABLE public.servidor
(
    id bigserial,
    fecha_ingreso date NOT NULL,
    fecha_salida date,
    regimen_laboral bigint NOT NULL,
    tipo_contrato bigint,
    persona bigint,
    CONSTRAINT servidor_pkey PRIMARY KEY (id),
    CONSTRAINT fk_persona FOREIGN KEY (persona)
        REFERENCES public.cliente (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_regimen_laboral FOREIGN KEY (regimen_laboral)
        REFERENCES public.regimen_laboral_contrato (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_tipo_contrato FOREIGN KEY (tipo_contrato)
        REFERENCES public.regimen_laboral_contrato (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.servidor
    OWNER to postgres;	
	


CREATE TABLE public.asociacion_presupuestaria
(
    id bigserial,
    cuenta_contable bigint,
	presupuesto_debito bigint,
	presupuesto_credito bigint,
    CONSTRAINT asociacion_presupuestaria_pkey PRIMARY KEY (id),
    CONSTRAINT asociacion_debito_cuenta_fkey FOREIGN KEY (presupuesto_debito)
        REFERENCES public.catalogo_presupuesto (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
		CONSTRAINT asociacion_credito_cuenta_fkey FOREIGN KEY (presupuesto_credito)
        REFERENCES public.catalogo_presupuesto (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT asociacion_presupuestaria_cuenta_contable_fkey FOREIGN KEY (cuenta_contable)
        REFERENCES public.cuenta_contable (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.asociacion_presupuestaria
    OWNER to postgres;	
		