package med.voll.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private String apiSecret;

    public String generarToken(Usuario usuario) {
        try {
            //aca iria la api secret que esta en la variable de entorno
            //Este algoritmo encripta el token y asegura su integridad,
            // de modo que solo quien tenga la clave pueda verificar su autenticidad.
            Algorithm algorithm = Algorithm.HMAC256("123456");
            return JWT.create(). // Inicia la creacion del token jwt
                    withIssuer("voll med"). // Define el emisor del token, la fuente generadora del token
                    withSubject(usuario.getEmail()). // añade el login como el sujeto del token
                    withClaim("id", usuario.getId()). // añade un dato, en este caso el id
                    withExpiresAt(generarFechaExpiracion()). // define la fecha de expiracion de ese token
                    sign(algorithm);// Firma el token con el algoritmo de la variable asegurando su autenticidad
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    //Recibe un parametro que es un json web token
    public String getSubject(String token){
        if(token == null){
            throw new RuntimeException();
        }
        DecodedJWT verifier = null;
        try {
            // Se establece este algoritmo con la clave secreta
            Algorithm algorithm = Algorithm.HMAC256("123456");
            //  Configura un verificador JWT utilizando el algoritmo definido.
            verifier = JWT.require(algorithm)
                    // : Verifica que el token tenga como emisor (iss) el valor "voll med".
                    .withIssuer("voll med")
                    // Construye la instancia del verificado JWT
                    .build()
                    // Verifica el token recibido contra las condiciones definidas
                    .verify(token);
            //Intenta obtener el sujeto del token, que generalmente es el usuario
            verifier.getSubject();


        } catch (JWTVerificationException exception){
            // Invalid signature/claims
        }
        if( verifier == null ||  verifier.getSubject() == null){
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
