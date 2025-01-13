package hub.forum.api.domain.formacao.batch.processors;

import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.formacao.Formacao;
import hub.forum.api.domain.formacao.batch.context.FormacaoJobContext;
import hub.forum.api.infra.exceptions.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormacaoItemProcessor implements ItemProcessor<FormacaoJobContext, FormacaoJobContext> {

    private final EscolaRepository escolaRepository;

    @Override
    public FormacaoJobContext process(FormacaoJobContext context) {
        var dados = context.getDadosCadastro();
        var escola = escolaRepository.findById(dados.escolaID())
                .orElseThrow(() -> new ValidacaoException("Escola não encontrada"));

        var formacao = new Formacao();
        formacao.setEscola(escola);
        formacao.setFormacao(dados.formacao());
        formacao.setAreaFormacao(dados.areaFormacao());
        formacao.setDescricao(dados.descricao());

        context.setFormacao(formacao);
        return context;
    }
}
