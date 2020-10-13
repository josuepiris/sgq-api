ALTER TABLE permissao MODIFY descricao VARCHAR(120) NOT NULL;

UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico)' WHERE codigo = 1000404;

INSERT INTO permissao (codigo, role_name, descricao) values (1000406, 'ROLE_1000406', 'Indicadores > Retrabalhos > Consultas > Estatísticas por Funcionário');
INSERT INTO permissao (codigo, role_name, descricao) values (1000407, 'ROLE_1000407', 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico) > Alterar filtro "Processo"');
INSERT INTO permissao (codigo, role_name, descricao) values (1000408, 'ROLE_1000408', 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico) > Alterar filtro "Departamento"');
INSERT INTO permissao (codigo, role_name, descricao) values (1000409, 'ROLE_1000409', 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico) > Alterar filtro "Funcionário"');
INSERT INTO permissao (codigo, role_name, descricao) values (1000410, 'ROLE_1000410', 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico) > Alterar filtro "Funcionário de Registro"');
INSERT INTO permissao (codigo, role_name, descricao) values (1000411, 'ROLE_1000411', 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho (analítico) > Visualizar Detalhes dos Registros');