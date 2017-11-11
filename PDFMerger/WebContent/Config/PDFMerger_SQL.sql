create database pdf_merger;

create table usuario(
    idUsuario int primary key,
    nome varchar (100),
    email varchar (100),
    login varchar (20),
    senha varchar (50),
    perfil varchar (20),
    trocasenha char (1),
    bloqueado char (1)
    );

create table arquivo(
    idArquivo int primary key,
    nome varchar (100),
    idUsuario int,
    caminho varchar (255),
    acesso char (1)
    );
	
alter table usuario add CONSTRAINT unique_email unique (email);
alter table usuario add CONSTRAINT unique_login unique (login);

create sequence id_usuario_seq;
create sequence id_arquivo_seq;

alter table usuario alter idUsuario set default nextval('id_usuario_seq');
alter table arquivo alter idArquivo set default nextval('id_arquivo_seq');

insert into usuario (nome, email, login, senha, perfil, trocaSenha, bloqueado)
values ('Administrador do sistema', 'admin.pdfm@gmail.com', 'admin', 'f865b53623b121fd34ee5426c792e5c33af8c227', 'Administrador', 'N', 'N');