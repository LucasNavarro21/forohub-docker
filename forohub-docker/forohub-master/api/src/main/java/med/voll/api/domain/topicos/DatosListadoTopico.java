package med.voll.api.domain.topicos;

public record DatosListadoTopico(Long id, String titulo, String mensaje, String autor, String curso) {

    public DatosListadoTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensaje(), topico.getUsuario().getNombre(),
                topico.getCursos().getNombre());
    }
}
