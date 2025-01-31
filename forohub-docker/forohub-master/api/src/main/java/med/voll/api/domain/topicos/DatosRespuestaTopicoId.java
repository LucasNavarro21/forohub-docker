package med.voll.api.domain.topicos;

import med.voll.api.domain.cursos.DatosRespuestaCurso;
import med.voll.api.domain.usuarios.DatosRespuestaUsuario;

public record DatosRespuestaTopicoId(Long id, String titulo, String mensaje, String fechaCreacion, String estado, DatosRespuestaUsuario autor,
                                     DatosRespuestaCurso curso) {

    public DatosRespuestaTopicoId(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getFechaCreacion().toString(),
                topico.getEstado().toString(), new DatosRespuestaUsuario(topico.getUsuario()),
                new DatosRespuestaCurso(topico.getCursos()));
    }
}