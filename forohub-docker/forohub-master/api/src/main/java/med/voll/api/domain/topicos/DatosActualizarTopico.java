package med.voll.api.domain.topicos;

import jakarta.validation.constraints.NotNull;

public record DatosActualizarTopico(
        @NotNull
        Long id,

        String titulo,

        String mensaje,

        Estado estado,

        Long autorId,

        Long cursoId) {
}
