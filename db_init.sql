CREATE USER postgres WITH PASSWORD 'postgres';
CREATE DATABASE Talaria;
GRANT ALL PRIVILEGES ON DATABASE Talaria TO postgres;
GRANT ALL ON schema public TO postgres;

