package med.voll.api.domain.topicos;

public record DatosRespuestaTopico(String titulo, String mensaje, String autor, String curso) {

    public DatosRespuestaTopico(Topico topico) {
        this(topico.getTitulo(), topico.getMensaje(), topico.getUsuario().getNombre(), topico.getCursos().getNombre());
    }
}
