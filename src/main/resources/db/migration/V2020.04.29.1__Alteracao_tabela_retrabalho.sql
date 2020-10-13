ALTER TABLE retrabalho ADD COLUMN divergencias VARCHAR(80) AFTER observacao;
ALTER TABLE retrabalho ADD COLUMN ignorar_divergencias BOOLEAN NOT NULL DEFAULT FALSE AFTER divergencias;
ALTER TABLE sgq.retrabalho MODIFY ignorar_duplicado BOOLEAN DEFAULT FALSE AFTER ignorar_divergencias;
