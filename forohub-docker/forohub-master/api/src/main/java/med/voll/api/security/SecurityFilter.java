package med.voll.api.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Marca esta clase como un componente de Spring para que pueda ser detectada y gestionada por el contenedor.
@Service
//Garantiza que este filtro se ejecute una vez por cada solicitud HTTP entrante.
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    //Se utiliza para recuperar los detalles del usuario desde la bd tomando el subject del token
    private UsuarioRepository usuarioRepository;
    @Autowired
    //maneja la validación y extracción de información de los tokens JWT.
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrae el token eliminando el prefijo "Bearer "
            var token = authHeader.replace("Bearer ", "");
            System.out.println("Token recibido: " + token);

            // Valida el token y extrae el subject
            var subject = tokenService.getSubject(token);
            if (subject != null) {
                System.out.println("Token válido, subject extraído: " + subject);

                // Busca al usuario en la base de datos
                var usuario = usuarioRepository.findByEmail(subject);

                // Manejar caso donde usuario es null
                if (usuario == null) {
                    System.out.println("Usuario no encontrado en la base de datos.");
                } else {
                    System.out.println("Usuario encontrado: " + usuario.getUsername());

                    // Crea el objeto de autenticación y configura el contexto de seguridad
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                System.out.println("El token no contiene un subject válido.");
            }
        } else {
            System.out.println("Encabezado Authorization no válido o no presente.");
        }

        // Continúa con el resto de los filtros
        filterChain.doFilter(request, response);
        }
     }