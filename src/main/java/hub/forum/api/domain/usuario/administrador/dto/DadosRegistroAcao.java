package hub.forum.api.domain.usuario.administrador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DadosRegistroAcao(
        @NotEmpty(message = "É necessário informar pelo menos uma ação")
        List<String> acoesExecutadas,

        @NotBlank(message = "Informe os detalhes da execução")
        @Pattern(
                regexp = "^[a-zA-ZÀ-ÖØ-öø-ÿ0-9.,:()\\-\\s]+$", message = "A mensagem deve conter apenas letras, numeros e espaços"
        )
        String detalhes
) {}