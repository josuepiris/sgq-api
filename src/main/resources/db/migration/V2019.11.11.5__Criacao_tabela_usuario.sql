CREATE TABLE usuario (
	email VARCHAR(40) UNIQUE,
	senha VARCHAR(60),
	codigo_autenticacao VARCHAR(6),
	funcionario_id BIGINT(20) NOT NULL,
FOREIGN KEY (funcionario_id) REFERENCES funcionario(funcionario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;