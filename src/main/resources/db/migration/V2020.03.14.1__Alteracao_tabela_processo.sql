ALTER TABLE processo ADD COLUMN tipo_processo VARCHAR(20) NOT NULL AFTER descricao;
ALTER TABLE processo ADD COLUMN codigo_mapeamento BIGINT(20) AFTER tipo_processo;