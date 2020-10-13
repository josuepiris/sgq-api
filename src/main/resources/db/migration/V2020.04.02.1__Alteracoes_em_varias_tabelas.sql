ALTER TABLE usuario CHANGE email user_id VARCHAR(40);

ALTER TABLE funcionario CHANGE email_gestor email VARCHAR(40);

ALTER TABLE departamento ADD COLUMN codigo_gestor BIGINT(20) NOT NULL DEFAULT 1 AFTER nome;
ALTER TABLE departamento ADD FOREIGN KEY (codigo_gestor) REFERENCES funcionario(funcionario_id);
ALTER TABLE departamento ALTER codigo_gestor DROP DEFAULT;
ALTER TABLE departamento DROP COLUMN email;