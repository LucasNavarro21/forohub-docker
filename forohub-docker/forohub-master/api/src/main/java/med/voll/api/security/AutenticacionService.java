package med.voll.api.security;

import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Override
    //Es el método requerido por UserDetailsService.
    // Se invoca cuando Spring Security necesita autenticar a un usuario.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Busca un usuario en la base de datos por su nombre de usuario.
        // El método debe devolver un objeto que implemente la interfaz UserDetails
        //  (probablemente una entidad llamada Usuario que extiende o implementa UserDetails)
        return usuarioRepository.findByEmail(email);
    }
}
