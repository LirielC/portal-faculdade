-- Inserindo dados na tabela matriculas_autorizadas
INSERT INTO matriculas_autorizadas (matricula, nome, email, tipo_usuario, data_criacao, ja_registrado) 
SELECT t.matricula, t.nome, t.email, t.tipo_usuario, t.data_criacao, t.ja_registrado
FROM (
    SELECT '2024002' as matricula, 'Maria Santos' as nome, 'maria.santos@email.com' as email, 'ALUNO' as tipo_usuario, NOW() as data_criacao, true as ja_registrado UNION ALL
    SELECT '2024003', 'Pedro Oliveira', 'pedro.oliveira@email.com', 'ALUNO', NOW(), false UNION ALL
    SELECT '2024004', 'Ana Costa', 'ana.costa@email.com', 'ALUNO', NOW(), false UNION ALL
    SELECT '2024005', 'Carlos Pereira', 'carlos.pereira@email.com', 'ALUNO', NOW(), true UNION ALL
    SELECT 'PROF001', 'Prof. Dr. José Silva', 'jose.silva@email.com', 'PROFESSOR', NOW(), false UNION ALL
    SELECT 'PROF002', 'Profa. Dra. Maria Santos', 'maria.santos.prof@email.com', 'PROFESSOR', NOW(), false UNION ALL
    SELECT 'PROF003', 'Prof. Dr. Pedro Oliveira', 'pedro.oliveira.prof@email.com', 'PROFESSOR', NOW(), false
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM matriculas_autorizadas WHERE matricula = t.matricula
);

-- Inserindo professores se não existirem
INSERT INTO usuarios (nome, matricula, email, senha, tipo_usuario, ativo, titulacao)
SELECT t.nome, t.matricula, t.email, t.senha, t.tipo_usuario, t.ativo, t.titulacao
FROM (
    SELECT 'Prof. Dr. José Silva' as nome, 'PROF001' as matricula, 'jose.silva@email.com' as email, 
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy' as senha, 
           'PROFESSOR' as tipo_usuario, true as ativo, 'Doutor em Computação' as titulacao UNION ALL
    SELECT 'Profa. Dra. Maria Santos', 'PROF002', 'maria.santos.prof@email.com', 
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy', 
           'PROFESSOR', true, 'Doutora em Matemática' UNION ALL
    SELECT 'Prof. Dr. Pedro Oliveira', 'PROF003', 'pedro.oliveira.prof@email.com', 
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy', 
           'PROFESSOR', true, 'Doutor em Física'
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE matricula = t.matricula
);

-- Inserindo disciplinas se não existirem
INSERT INTO disciplinas (nome, descricao, carga_horaria, sala, dia_semana, horario_inicio, horario_fim, professor_id)
SELECT t.nome, t.descricao, t.carga_horaria, t.sala, t.dia_semana, t.horario_inicio, t.horario_fim, t.professor_id
FROM (
    SELECT 'Algoritmos e Programação' as nome, 'Introdução à programação e lógica de programação' as descricao, 
           60 as carga_horaria, 'Lab 01' as sala, 'Segunda-feira' as dia_semana, 
           '08:00:00' as horario_inicio, '10:00:00' as horario_fim, 1 as professor_id UNION ALL
    SELECT 'Cálculo I', 'Limites, derivadas e integrais', 60, 'Sala 101', 'Terça-feira', '10:00:00', '12:00:00', 2 UNION ALL
    SELECT 'Introdução à Computação', 'Conceitos básicos de computação', 60, 'Lab 02', 'Quarta-feira', '14:00:00', '16:00:00', 1 UNION ALL
    SELECT 'Matemática Discreta', 'Lógica, conjuntos e relações', 60, 'Sala 102', 'Quinta-feira', '08:00:00', '10:00:00', 2 UNION ALL
    SELECT 'Comunicação e Expressão', 'Comunicação técnica e acadêmica', 60, 'Sala 103', 'Sexta-feira', '10:00:00', '12:00:00', 3 UNION ALL
    SELECT 'Programação Orientada a Objetos', 'Conceitos de POO e Java', 60, 'Lab 01', 'Segunda-feira', '14:00:00', '16:00:00', 1 UNION ALL
    SELECT 'Arquitetura de Computadores', 'Organização e arquitetura de computadores', 60, 'Lab 03', 'Terça-feira', '16:00:00', '18:00:00', 3 UNION ALL
    SELECT 'Probabilidade e Estatística', 'Conceitos básicos de estatística', 60, 'Sala 104', 'Quarta-feira', '08:00:00', '10:00:00', 2 UNION ALL
    SELECT 'Álgebra Linear', 'Matrizes, vetores e transformações lineares', 60, 'Sala 105', 'Quinta-feira', '14:00:00', '16:00:00', 2
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM disciplinas WHERE nome = t.nome
);

-- Inserindo alunos se não existirem
INSERT INTO usuarios (nome, matricula, email, senha, tipo_usuario, ativo, ano_ingresso)
SELECT t.nome, t.matricula, t.email, t.senha, t.tipo_usuario, t.ativo, t.ano_ingresso
FROM (
    SELECT 'João Silva' as nome, '2024001' as matricula, 'joao.silva@email.com' as email, 
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy' as senha, 
           'ALUNO' as tipo_usuario, true as ativo, 2023 as ano_ingresso UNION ALL
    SELECT 'Maria Santos', '2024002', 'maria.santos@email.com', 
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy', 
           'ALUNO', true, 2023 UNION ALL
    SELECT 'Carlos Pereira', '2024005', 'carlos.pereira@email.com',
           '$2a$10$n8qU69qaRGS3kLGQqXKqEOOZvBBQOLGEMBDJ5XkRFbNtXF6wGD.Uy',
           'ALUNO', true, 2023
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE matricula = t.matricula
);

-- Inserindo histórico se não existir
INSERT INTO historico_aluno (aluno_id, disciplina_id, nota1, nota2, nota_final, status, semestre_ano, data_conclusao)
SELECT t.aluno_id, t.disciplina_id, t.nota1, t.nota2, t.nota_final, t.status, t.semestre_ano, t.data_conclusao
FROM (
    -- João - 2023.2
    SELECT 1 as aluno_id, 1 as disciplina_id, 8.0 as nota1, 9.0 as nota2, 8.5 as nota_final, 
           'APROVADO' as status, '2023.2' as semestre_ano, DATEADD('MONTH', -4, NOW()) as data_conclusao UNION ALL
    SELECT 1, 2, 5.0, 6.0, 5.5, 'REPROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 1, 3, 8.5, 9.5, 9.0, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 1, 4, 7.5, 8.5, 8.0, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 1, 5, 7.3, 8.3, 7.8, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    -- João - 2024.1
    SELECT 1, 6, 8.2, 9.2, 8.7, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 1, 2, 6.7, 7.7, 7.2, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 1, 7, 7.8, 8.8, 8.3, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 1, 8, 6.3, 7.3, 6.8, 'REPROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 1, 9, 7.0, 8.0, 7.5, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    -- Maria - 2023.2
    SELECT 2, 1, 9.0, 9.5, 9.3, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 2, 2, 8.0, 8.5, 8.3, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 2, 3, 9.5, 9.0, 9.3, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 2, 4, 8.5, 9.0, 8.8, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 2, 5, 8.3, 8.8, 8.6, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    -- Maria - 2024.1
    SELECT 2, 6, 9.2, 9.5, 9.4, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 2, 7, 8.8, 9.2, 9.0, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 2, 8, 8.3, 8.8, 8.6, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW()) UNION ALL
    SELECT 2, 9, 9.0, 9.5, 9.3, 'APROVADO', '2024.1', DATEADD('MONTH', -1, NOW())
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM historico_aluno 
    WHERE aluno_id = t.aluno_id 
    AND disciplina_id = t.disciplina_id 
    AND semestre_ano = t.semestre_ano
);

-- Inserindo histórico para Carlos Pereira
INSERT INTO historico_aluno (aluno_id, disciplina_id, nota1, nota2, nota_final, status, semestre_ano, data_conclusao)
SELECT t.aluno_id, t.disciplina_id, t.nota1, t.nota2, t.nota_final, t.status, t.semestre_ano, t.data_conclusao
FROM (
    -- Carlos Pereira - 2023.2
    SELECT 5 as aluno_id, 1 as disciplina_id, 7.5 as nota1, 8.5 as nota2, 8.0 as nota_final, 
           'APROVADO' as status, '2023.2' as semestre_ano, DATEADD('MONTH', -4, NOW()) as data_conclusao UNION ALL
    SELECT 5, 2, 8.0, 8.5, 8.3, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 5, 3, 7.0, 7.5, 7.3, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    SELECT 5, 4, 6.5, 7.0, 6.8, 'APROVADO', '2023.2', DATEADD('MONTH', -4, NOW()) UNION ALL
    -- Carlos Pereira - 2024.1 (Em andamento)
    SELECT 5, 5, 8.5, NULL, NULL, 'EM_ANDAMENTO', '2024.1', NULL UNION ALL
    SELECT 5, 6, 7.5, NULL, NULL, 'EM_ANDAMENTO', '2024.1', NULL UNION ALL
    SELECT 5, 7, 9.0, NULL, NULL, 'EM_ANDAMENTO', '2024.1', NULL UNION ALL
    SELECT 5, 8, 8.0, NULL, NULL, 'EM_ANDAMENTO', '2024.1', NULL
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM historico_aluno 
    WHERE aluno_id = t.aluno_id 
    AND disciplina_id = t.disciplina_id 
    AND semestre_ano = t.semestre_ano
);

-- Inserindo matrículas atuais (disciplinas em andamento) para todos os alunos
INSERT INTO matriculas (aluno_id, disciplina_id, data_matricula, status, semestre_ano)
SELECT t.aluno_id, t.disciplina_id, CURRENT_DATE(), 'ATIVA', t.semestre_ano
FROM (
    -- Carlos Pereira - Disciplinas atuais
    SELECT 5 as aluno_id, 5 as disciplina_id, '2024.1' as semestre_ano UNION ALL
    SELECT 5, 6, '2024.1' UNION ALL
    SELECT 5, 7, '2024.1' UNION ALL
    SELECT 5, 8, '2024.1' UNION ALL
    -- João Silva - Disciplinas atuais
    SELECT 1, 6, '2024.1' UNION ALL
    SELECT 1, 7, '2024.1' UNION ALL
    SELECT 1, 8, '2024.1' UNION ALL
    SELECT 1, 9, '2024.1' UNION ALL
    -- Maria Santos - Disciplinas atuais
    SELECT 2, 6, '2024.1' UNION ALL
    SELECT 2, 7, '2024.1' UNION ALL
    SELECT 2, 8, '2024.1' UNION ALL
    SELECT 2, 9, '2024.1'
) AS t
WHERE NOT EXISTS (
    SELECT 1 FROM matriculas 
    WHERE aluno_id = t.aluno_id 
    AND disciplina_id = t.disciplina_id
); 