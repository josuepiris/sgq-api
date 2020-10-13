CREATE TABLE retrabalho (
codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
protocolo VARCHAR(13) NOT NULL,
codigo_processo BIGINT(20) NOT NULL,
codigo_motivo BIGINT(20) NOT NULL,
codigo_funcionario BIGINT(20) NOT NULL,
codigo_departamento BIGINT(20) NOT NULL,
observacao VARCHAR(255),
funcionario_registro BIGINT(20) NOT NULL,
funcionario_alteracao BIGINT(20),
ignorar_duplicado BOOLEAN NOT NULL DEFAULT FALSE,
data_hora_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
data_hora_alteracao DATETIME ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (codigo_motivo) REFERENCES motivo_retrabalho(codigo),
FOREIGN KEY (codigo_funcionario) REFERENCES funcionario(funcionario_id),
FOREIGN KEY (codigo_departamento) REFERENCES departamento(codigo),
FOREIGN KEY (funcionario_registro) REFERENCES funcionario(funcionario_id),
FOREIGN KEY (funcionario_alteracao) REFERENCES funcionario(funcionario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;