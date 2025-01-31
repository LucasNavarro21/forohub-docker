package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuarios.DatosAutenticacionUsuario;
import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.security.TokenService;
import med.voll.api.security.DatosJWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Define estas clase como controlador web que responde a solicitud http
//Cualquier metodo dentro de esta clase devulve el resultado dentro del cuerpo http en json
@RequestMapping("/login")
public class AutenticationController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping
    //Define un metodo llamado autenticarUsuario que recibe un objeto DatosAutenticacionUsuario en el cuerpo
    //de la solicitud  @RequestBody
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario){
        //  Aquí, se crea un objeto UsernamePasswordAuthenticationToken,
        // que implementa la interfaz Authentication de Spring Security.

        //Crea un token de autenticación con el nombre de usuario (login) y la contraseña (clave).
        // Este token implementa la interfaz Authentication de Spring Security.
        Authentication authtoken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.nombre(), datosAutenticacionUsuario.contrasena());

        //Intenta autenticar al usuario con los datos de authtoken
        var usuarioAutenticado = authenticationManager.authenticate(authtoken);

        //Llama al servicio TokenService para generar un token JWT, el cual contiene información de autenticación sobre el usuario
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }

}
