-- Adicionar novas colunas
ALTER TABLE disciplinas ADD COLUMN dia_semana VARCHAR(255) NOT NULL DEFAULT 'Segunda-feira';
ALTER TABLE disciplinas ADD COLUMN horario_inicio TIME NOT NULL DEFAULT '08:00:00';
ALTER TABLE disciplinas ADD COLUMN horario_fim TIME NOT NULL DEFAULT '10:00:00';

-- Migrar dados do campo horario para os novos campos (se existirem dados)
UPDATE disciplinas SET horario_inicio = horario WHERE horario IS NOT NULL;
UPDATE disciplinas SET horario_fim = ADDTIME(horario, '02:00:00') WHERE horario IS NOT NULL;

-- Remover a coluna antiga
ALTER TABLE disciplinas DROP COLUMN horario; 