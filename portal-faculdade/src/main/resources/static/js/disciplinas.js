// Funções para controle das tabs
function showTab(tabName) {
    // Esconde todas as tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.style.display = 'none';
    });
    
    // Remove a classe ativa de todos os botões
    document.querySelectorAll('.tab-trigger').forEach(button => {
        button.classList.remove('active');
    });
    
    // Mostra a tab selecionada
    document.getElementById('tab-' + tabName).style.display = 'block';
    
    // Adiciona a classe ativa ao botão selecionado
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
}

// Função para confirmar cancelamento de matrícula
function confirmarCancelamento(disciplinaId, disciplinaNome) {
    return confirm(`Tem certeza que deseja cancelar a matrícula em ${disciplinaNome}?`);
}

// Função para abrir modal de troca de disciplina
function abrirModalTroca(disciplinaAtualId) {
    document.getElementById(`trocarModal${disciplinaAtualId}`).style.display = 'block';
}

// Função para fechar modal de troca de disciplina
function fecharModalTroca(disciplinaAtualId) {
    document.getElementById(`trocarModal${disciplinaAtualId}`).style.display = 'none';
}

// Função para confirmar troca de disciplina
function confirmarTroca(disciplinaAtualId, disciplinaAtualNome, novaDisciplinaId, novaDisciplinaNome) {
    return confirm(`Tem certeza que deseja trocar a disciplina ${disciplinaAtualNome} por ${novaDisciplinaNome}?`);
}

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    // Mostra a primeira tab por padrão
    showTab('matriculadas');
    
    // Configura os tooltips do Bootstrap
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Configura os modais do Bootstrap
    var modalTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="modal"]'));
    modalTriggerList.map(function (modalTriggerEl) {
        return new bootstrap.Modal(modalTriggerEl);
    });
}); 