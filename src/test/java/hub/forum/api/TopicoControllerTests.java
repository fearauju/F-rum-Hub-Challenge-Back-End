package hub.forum.api;

import hub.forum.api.domain.curso.Curso;
import hub.forum.api.domain.topico.DadosCadastroTopico;
import hub.forum.api.domain.topico.DadosDetalhamentoTopico;
import hub.forum.api.domain.topico.TopicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static hub.forum.api.domain.formacao.AreaFormacao.PROGRAMACAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicoControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroTopico> dadosCadastroTopicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoTopico> dadosDetalhamentoTopicoJson;

    @Mock
    private TopicoService service;

    @Test
    @DisplayName("Deveria retornar código 400 quando informações estão inválidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc.perform(post("/topicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria retornar código 201 quando informações estão válidas")
    @WithMockUser(roles = "PROFESSOR")
    void cadastrar_cenario2() throws Exception {
        var dadosCadastro = new DadosCadastroTopico(
                1L, 1L, "Java",
                "Dúvida sobre Spring Boot com mais de 20 caracteres",
                "Esta é uma mensagem de dúvida sobre Spring Boot que precisa ter mais de 100 caracteres para passar na validação. Estou com dificuldades em entender como fazer os testes unitários."
        );

        var dadosDetalhamento = new DadosDetalhamentoTopico(
                1L, 1L, PROGRAMACAO,
                new Curso(), // configure o curso adequadamente
                dadosCadastro.titulo(),
                dadosCadastro.mensagem(),
                LocalDateTime.now()
        );

        when(service.criarTopico(any())).thenReturn(dadosDetalhamento);

        var response = mvc.perform(
                post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroTopicoJson.write(dadosCadastro).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                dadosDetalhamentoTopicoJson.write(dadosDetalhamento).getJson()
        );
    }
}