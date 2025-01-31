package med.voll.api.domain.respuestas;

import med.voll.api.domain.topicos.DatosRespuestaTopico;
import med.voll.api.domain.usuarios.DatosRespuestaUsuario;

public record DatosRetornoRespuestaId(String mensaje, DatosRespuestaTopico topico, String fechaCreacion, DatosRespuestaUsuario autor, String solucion) {

    public DatosRetornoRespuestaId(Respuesta respuesta) {
        this(respuesta.getMensaje(), new DatosRespuestaTopico(respuesta.getTopico()), respuesta.getFechaCreacion().toString(),
                new DatosRespuestaUsuario(respuesta.getAutor()), respuesta.getSolucion().toString());
    }
}
