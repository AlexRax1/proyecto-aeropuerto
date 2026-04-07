--auth
CREATE TABLE rol_user( 
	rol_user_id serial primary key, 
	nombre_rol varchar(30)
);

CREATE TABLE credenciales( 
	user_id serial primary key, 
	password varchar(255),
	rol_id int,
	fecha_creacion timestamp, 
	usuario_creacion varchar(150),
	fecha_modificacion timestamp,
	usuario_modificacion varchar(150),
	constraint user_rol foreign key (rol_id) references rol_user(rol_user_id)
);

CREATE TABLE bitacora (
	registro_id serial primary key,
	user_id int references credenciales(user_id), 
	fecha_hora_regitro timestamp,
	accion varchar(100)
);