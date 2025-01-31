package med.voll.api.domain.usuarios;

public record DatosActualizarUsuario(Long id, String nombre, String email, String contrasena, Tipo tipo) {
}
