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
