# Demo - Conexão com banco de dados

## Como subir
Primeiro você vai precisar de um docker, para iniciar o container do postgres apenas rode o comando

``docker run -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres``

Este comando vai gerar uma instância de postgres limpa com usuário e senha postgres na porta padrão

## Comandos utilizados nos exemplos

```
CREATE TABLE empresa (
    id_empresa SERIAL PRIMARY KEY ,
    nome varchar(45),
    cnpj varchar(14)
);

INSERT INTO empresa(nome, cnpj) values ('empresa do ricardo', '78635978000117');
select * from empresa;

SELECT * FROM pg_stat_activity where backend_type = 'client backend';

PREPARE insertEmpresa (varchar, varchar) AS
    INSERT INTO empresa (nome, cnpj) VALUES($1, $2);

EXECUTE insertEmpresa('empresa do william', '78635978000118');
EXECUTE insertEmpresa('empresa do nicholas', '78635978000119');
EXECUTE insertEmpresa('empresa do gabriel', '78635978000120');
```

