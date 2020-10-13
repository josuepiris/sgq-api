UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho' WHERE codigo = 1000404;
UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho > Alterar filtro "Processo"' WHERE codigo = 1000407;
UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho > Alterar filtro "Departamento"' WHERE codigo = 1000408;
UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho > Alterar filtro "Funcionário"' WHERE codigo = 1000409;
UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho > Alterar filtro "Funcionário de Registro"' WHERE codigo = 1000410;
UPDATE permissao SET descricao = 'Indicadores > Retrabalhos > Consultas > Registros de Retrabalho > Visualizar Detalhes dos Registros' WHERE codigo = 1000411;

INSERT INTO permissao (codigo, role_name, descricao) values (1000412, 'ROLE_1000412', 'Indicadores > Retrabalhos > Consultas > Estatísticas por Funcionário > Alterar filtro "Processo"');
INSERT INTO permissao (codigo, role_name, descricao) values (1000413, 'ROLE_1000413', 'Indicadores > Retrabalhos > Consultas > Estatísticas por Funcionário > Alterar filtro "Departamento"');

INSERT INTO permissao (codigo, role_name, descricao) values (1000414, 'ROLE_1000414', 'Indicadores > Retrabalhos > Gestor / Excluir Registros Criados por Funcionários do Departamento');
