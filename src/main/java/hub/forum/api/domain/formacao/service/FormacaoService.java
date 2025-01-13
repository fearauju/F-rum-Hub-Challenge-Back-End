package hub.forum.api.domain.formacao.service;


import hub.forum.api.domain.curso.repository.CursoRepository;
import hub.forum.api.domain.escola.EscolaRepository;
import hub.forum.api.domain.formacao.*;
import hub.forum.api.domain.formacao.dto.DadosAtualizacaoFormacao;
import hub.forum.api.domain.formacao.dto.DadosCadastroFormacao;
import hub.forum.api.domain.formacao.dto.DadosDetalhamentoFormacao;
import hub.forum.api.domain.formacao.dto.DadosListagemFormacao;
import hub.forum.api.domain.formacao.repository.FormacaoRepository;
import hub.forum.api.domain.formacao.validacao.DadosValidacaoFormacao;

import hub.forum.api.domain.usuario.professor.ProfessorRepository;
import hub.forum.api.domain.util.ValidadorBase;
import hub.forum.api.infra.exceptions.ValidacaoException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@Slf4j
public class FormacaoService {

    @Autowired
    private FormacaoRepository formacaoRepository;

    @Autowired
    private EscolaRepository escolaRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private List<ValidadorBase<DadosValidacaoFormacao>> validadores;

    @Autowired
    private EntityManager entityManager;

    // Separar em várias transações foi uma forma de evitar conflitos de concorrências que ocorre quando o hibernate tenta atualizar a mesma entidade várias vezes,
    // Entretanto, neste projeto na segunda tentativa de carregamento já ocorria esse erro: ObjectOptimisticLockingFailureException.
    // Várias tentativas isolar e manter uma transação única para cada etapa, que funcionou em várias operações, mas no final de salvar a operação principal ocorria sempre o mesmo erro.
    // Por última tentativa é tentar a possibilidade de usar o Spring Batch para tentar contornar essa limitação, eu acho que é isso.

    private  JobLauncher jobLauncher;
    private  Job cadastrarFormacaoJob;

    public DadosDetalhamentoFormacao cadastrarFormacao(DadosCadastroFormacao dados) {
        try {
            JobParameters parameters = new JobParametersBuilder()
                    .addString("formacaoId", UUID.randomUUID().toString())
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            var executionContext = new ExecutionContext();
            executionContext.put("dadosCadastro", dados);

            JobExecution execution = jobLauncher.run(cadastrarFormacaoJob, parameters);

            if (execution.getStatus() == BatchStatus.COMPLETED) {
                Formacao formacao = (Formacao) execution.getExecutionContext()
                        .get("formacao");
                return new DadosDetalhamentoFormacao(formacao);
            }

            throw new ValidacaoException("Erro ao processar formação");

        } catch (Exception e) {
            log.error("Erro ao executar job de cadastro de formação", e);
            throw new ValidacaoException("Erro ao processar formação");
        }
    }


    public Page<DadosListagemFormacao> listarFormacoes(Pageable paginacao) {
        return formacaoRepository.findAll(paginacao).map(DadosListagemFormacao::new);
    }


    @Transactional
    public DadosDetalhamentoFormacao atualizarFormacao(DadosAtualizacaoFormacao dados, Long formacaoID) {

        var formacao = formacaoRepository.getReferenceById(formacaoID);
        formacao.atualizarInformacoes(dados);

        log.info("Formação ID {} atualizada", formacao.getId());
        return new DadosDetalhamentoFormacao(formacao);
    }
}
