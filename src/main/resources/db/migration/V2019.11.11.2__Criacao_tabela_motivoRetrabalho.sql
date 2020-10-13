CREATE TABLE motivo_retrabalho (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(60) NOT NULL,
	erro_critico BOOLEAN NOT NULL DEFAULT FALSE,
	codigo_processo BIGINT(20) NOT NULL,
FOREIGN KEY (codigo_processo) REFERENCES processo(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;