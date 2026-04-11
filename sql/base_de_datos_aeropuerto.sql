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

--usuario

CREATE TABLE usuarios(
	usuario_id serial primary key,
	user_id int, -- ¡OJO! Ya NO lleva FOREIGN KEY porque credenciales está en db_auth
	nombre varchar(50),
	num_pasaporte varchar(30),
	fecha_nacimiento date,
	nacionalidad varchar(30),
	extension_telefonica varchar(10),
	telefono varchar(20),
	correo varchar(150) unique,
	direccion varchar(100),
	extension_num_emergencias varchar(10),
	num_emergencias varchar(20),
	estado varchar(30),
	fecha_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_modificacion timestamp,
	usuario_modificacion varchar(150)
);


--operaciones


--aerolinea

create table aerolineas(
	aerolinea_id serial primary key,
	nombre_aerolinea varchar(100),
	cant_aviones int

);

create table destinos_aeropuertos(
	destino_id serial primary key,
	ciudad_destino varchar(100),
	pais_destino varchar(100),
	nombre_aeropuerto varchar(100)
);


create table destino_asignacion(
	asignacion_id serial primary key,
	aerolinea_id int,
	destino_id int,
	foreign key (aerolinea_id) references aerolineas(aerolinea_id),
	foreign key (destino_id) references destinos_aeropuertos(destino_id)

);



create table modelo_avion(
	modelo_avion_id serial primary key,
	modelo_avion_nombre varchar(100),
	cant_filas int,
	cant_columnas int,
	mapa_columnas varchar(50)--"ABC-DEF" "AB-CD|EF-GH" distinguir pasillos y pisos en avion(falta refinar detalles)
	--cant pisos, filas columnas segundo piso, cantidad de filas columnas dispareja entre 2 pisos
);

create table aviones(
	avion_id serial primary key,
	aerolinea_id int,
	modelo_avion_id int,
	marca varchar(100),
	ano varchar(4),
	cant_asientos_economica int,
	cant_asientos_ejecutiva int,
	cant_vuelos int,
	estado varchar(30),--activo/inactivo, no maneja si el avion esta en vuelo u otras cosas


	fecha_hora_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_hora_modificacion timestamp,
	usuario_modificacion varchar(150)


);

create table vuelos(
	vuelo_id serial primary key,
	avion_id int,
	origen int references destinos_aeropuertos(destino_id),
	destino int references destinos_aeropuertos(destino_id),
	fecha_salida date,
	hora_salida time,
	fecha_llegada date,
	hora_llegada time,
	estado varchar(50),
	precio_clase_economica numeric(10,2),
	precio_clase_ejecutiva numeric(10,2),
	monto_extra_ventana numeric(10,2),
	montro_extra_pasillo numeric(10,2),
	asientos_disponibles int, --por si se venden pasajes sin asignar asientos


	fecha_hora_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_hora_modificacion timestamp,
	usuario_modificacion varchar(150),

	foreign key (avion_id) references aviones(avion_id)
);


--asignacion de personal al vuelo
create table roles_tripulacion(
	rol_id serial primary key,
	nombre_rol_tripulacion varchar(30)
);


create table personal_tripulacion(
	personal_tripulacion_id serial primary key,
	aerolinea_id int,
	rol_id int,
	nombre varchar(50),
	estado varchar(30),--mejorar el manejo de estados


	fecha_hora_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_hora_modificacion timestamp,
	usuario_modificacion varchar(150),

	foreign key (aerolinea_id) references aerolineas(aerolinea_id),
	foreign key (rol_id) references roles_tripulacion(rol_id)
);


create table asignacion_tripulacion(
	asignacion_id serial primary key,
	vuelo_id int,
	tripulacion_id int,
	foreign key (vuelo_id) references vuelos(vuelo_id),
	foreign key (tripulacion_id) references personal_tripulacion(personal_tripulacion_id)
);


--gestion


create table asientos(
	asiento_id serial primary key,
	avion_id int references aviones(avion_id),
	fila_asiento varchar(10),--'1','2', '3'
	columna_asiento varchar(10),-- 'A','B', 'C'
	categoria_asiento varchar(20),
	tipo_asiento varchar(30),--si est ventana/pasillo/salida de emergencia.....
	estado varchar(30) --manejo
);





--db_reservas



CREATE TABLE boleto(
	boleto_id serial primary key,
	vuelo_id int,
	usuario_id int,
	asiento_id int,
	estado varchar(30),
	cant_maletas int,
	escala int references boleto(boleto_id),
	costo_boleto numeric(10,2),
	fecha_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_modificacion timestamp,
	usuario_modificacion varchar(150)
);

CREATE TABLE equipaje(
	equipaje_id serial primary key,
	boleto_id int references boleto(boleto_id),
	maleta varchar(30),
	peso numeric(6,2),
	fecha_creacion timestamp,
	usuario_creacion varchar(150),
	fecha_modificacion timestamp,
	usuario_modificacion varchar(150)
);
