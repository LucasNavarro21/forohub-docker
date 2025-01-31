package med.voll.api.domain.usuarios;

public record DatosRespuestaUsuario(String nombre, String correoElectronico, String tipo) {

    public DatosRespuestaUsuario(Usuario usuario) {
        this(usuario.getNombre(), usuario.getEmail(), usuario.getTipo().toString());
    }
}
