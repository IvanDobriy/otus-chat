CREATE TABLE public.users (
	user_id numeric(22) NOT NULL,
	login varchar(64) NOT NULL,
	"password" varchar(64) NULL,
	user_name varchar(128) NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT users_unique UNIQUE (user_name),
	CONSTRAINT users_unique_1 UNIQUE (login)
);

CREATE TABLE public.role_types (
	role_type_id numeric(22) NOT NULL,
	role_name varchar(100) NOT NULL,
	CONSTRAINT role_types_pk PRIMARY KEY (role_type_id)
);

CREATE TABLE public.users_roles (
	users_roles_id numeric(22) NOT NULL,
	user_id numeric(22) NULL,
	role_type_id numeric(22) NULL,
	CONSTRAINT users_roles_pk PRIMARY KEY (users_roles_id)
);
CREATE INDEX users_roles_role_type_id_idx ON public.users_roles USING btree (role_type_id);
CREATE INDEX users_roles_user_id_idx ON public.users_roles USING btree (user_id);


-- public.users_roles внешние включи

ALTER TABLE public.users_roles ADD CONSTRAINT users_roles_role_types_fk FOREIGN KEY (role_type_id) REFERENCES public.role_types(role_type_id);
ALTER TABLE public.users_roles ADD CONSTRAINT users_roles_users_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id);



CREATE TABLE public.restriction (
	restriction_id numeric(22) NOT NULL,
	user_id numeric(22) NULL,
	restriction_type_id numeric(22) NULL,
	CONSTRAINT restriction_pk PRIMARY KEY (restriction_id)
);
CREATE INDEX restriction_restriction_type_id_idx ON public.restriction USING btree (restriction_type_id);
CREATE INDEX restriction_user_id_idx ON public.restriction USING btree (user_id);


-- public.restriction внешние включи

ALTER TABLE public.restriction ADD CONSTRAINT restriction_restriction_type_fk FOREIGN KEY (restriction_type_id) REFERENCES public.restriction_type(restriction_type_id);
ALTER TABLE public.restriction ADD CONSTRAINT restriction_users_fk FOREIGN KEY (user_id) REFERENCES public.users(user_id);



CREATE TABLE public.restriction_type (
	restriction_type_id numeric(22) NOT NULL,
	restiction_name varchar(100) NULL,
	CONSTRAINT restriction_type_pk PRIMARY KEY (restriction_type_id)
);