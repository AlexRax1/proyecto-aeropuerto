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