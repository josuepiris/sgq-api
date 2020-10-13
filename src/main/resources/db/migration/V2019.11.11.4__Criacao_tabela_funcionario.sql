CREATE TABLE funcionario (
	funcionario_id BIGINT(20) PRIMARY KEY,
	nome VARCHAR(60) NOT NULL,
	email_gestor VARCHAR(40),
	codigo_departamento BIGINT(20),
	observacao VARCHAR(255),
	ativo BOOLEAN NOT NULL DEFAULT TRUE,
FOREIGN KEY (codigo_departamento) REFERENCES departamento(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
