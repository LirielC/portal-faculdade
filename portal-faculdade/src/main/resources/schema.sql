-- Primeiro, dropar as tabelas que têm chaves estrangeiras
DROP TABLE IF EXISTS presencas;
DROP TABLE IF EXISTS historico_aluno;
DROP TABLE IF EXISTS matriculas;
DROP TABLE IF EXISTS curso_disciplina;
DROP TABLE IF EXISTS disciplinas;
DROP TABLE IF EXISTS cursos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS matriculas_autorizadas;

-- Agora criar as tabelas na ordem correta
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(20) UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo_usuario VARCHAR(20) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ano_ingresso INT,
    titulacao VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    carga_horaria INT NOT NULL,
    duracao_semestres INT NOT NULL
);

CREATE TABLE IF NOT EXISTS disciplinas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    carga_horaria INT NOT NULL,
    sala VARCHAR(50) NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    horario_inicio TIME NOT NULL,
    horario_fim TIME NOT NULL,
    professor_id BIGINT,
    FOREIGN KEY (professor_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS curso_disciplina (
    curso_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    PRIMARY KEY (curso_id, disciplina_id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);

CREATE TABLE IF NOT EXISTS matriculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    data_matricula DATE NOT NULL,
    nota1 DOUBLE,
    nota2 DOUBLE,
    nota_final DOUBLE,
    status VARCHAR(20) NOT NULL,
    semestre_ano VARCHAR(10) NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES usuarios(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);

CREATE TABLE IF NOT EXISTS historico_aluno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    nota1 DOUBLE,
    nota2 DOUBLE,
    nota_final DOUBLE,
    status VARCHAR(20) NOT NULL,
    semestre_ano VARCHAR(10),
    data_conclusao DATETIME,
    FOREIGN KEY (aluno_id) REFERENCES usuarios(id),
    FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id)
);

CREATE TABLE IF NOT EXISTS presencas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    data_aula DATETIME NOT NULL,
    presente BOOLEAN NOT NULL,
    total_aulas INT,
    aulas_presentes INT,
    percentual_presenca DOUBLE,
    FOREIGN KEY (matricula_id) REFERENCES matriculas(id)
);

CREATE TABLE IF NOT EXISTS matriculas_autorizadas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    tipo_usuario VARCHAR(20) NOT NULL,
    data_criacao DATETIME,
    ja_registrado BOOLEAN DEFAULT FALSE
); 