#!/bin/bash
set -e

echo "Iniciando la creación de múltiples bases de datos..."

# 1. Crear las 4 bases de datos vacías
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE db_auth;
    CREATE DATABASE db_usuarios;
    CREATE DATABASE db_operaciones;
    CREATE DATABASE db_reservas;
EOSQL

echo "Bases de datos creadas. Inyectando tablas..."

# 2. Ejecutar cada script SQL en su base de datos respectiva
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "db_auth" -f /scripts/db_auth.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "db_usuarios" -f /scripts/db_usuarios.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "db_operaciones" -f /scripts/db_operaciones.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "db_reservas" -f /scripts/db_reservas.sql

echo "¡Todas las bases de datos están listas!"