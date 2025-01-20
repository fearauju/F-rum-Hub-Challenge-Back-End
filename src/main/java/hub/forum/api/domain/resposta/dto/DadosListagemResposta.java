package hub.forum.api.domain.resposta.dto;

import hub.forum.api.domain.resposta.Resposta;

import java.time.LocalDateTime;

public record DadosListagemResposta(
        Long respostaId,
        String mensagem,
        LocalDateTime dataCriacao,
        String autorNome
) {
    public DadosListagemResposta(Resposta resposta) {
        this(
                resposta.getId(),
                resposta.getResposta(),
                resposta.getDataCriacao(),
                resposta.getAutor().getPerfil() != null ?
                        resposta.getAutor().getPerfil().getNome() :
                        "Usuário sem perfil"
        );
    }
}