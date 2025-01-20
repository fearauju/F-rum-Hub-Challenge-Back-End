package hub.forum.api.domain.usuario;

public enum OperacaoDominio {
    // Operações de Usuário
    INATIVACAO,
    REATIVACAO,
    DESBLOQUEIO,
    ATUALIZACAO_LOGIN,

    // Operações de Professor
    CADASTRO_PROFESSOR,
    ATUALIZACAO_PROFESSOR,

    // Operações de Suporte
    CADASTRO_SUPORTE,
    ATUALIZACAO_SUPORTE
}