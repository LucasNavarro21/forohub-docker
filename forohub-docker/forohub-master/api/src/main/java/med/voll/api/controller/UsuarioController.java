package med.voll.api.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.usuarios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    //Los datos que se envian al json tienen que mapear al objeto de datosregistroUsuario
    public ResponseEntity registrar(@RequestBody  @Valid DatosRegistroUsuario datosRegistro, UriComponentsBuilder uri){
        //Guarda en la bd los datos asignados en usuario
        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistro));
        //Guarda en datosRespuesta los datos seleccionados encontrados en datosrespuestaUsuario
        DatosRespuestaUsuario datosRespuesta = new DatosRespuestaUsuario(usuario);
        //Construye una URL base que apunta al recurso recién creado, incluyendo un placeholder {id}.
        //.buildAndExpand(usuario.getId()).toUri(); = sustituye el id con el id recien creado
        //Convierte la URL en un objeto URI. = convierte la url en objeto
        URI url = uri.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();

        // Devuelve un código HTTP 201 Created indicando que se ha creado un
        // nuevo recurso. También incluye un encabezado Location que contiene la URL del nuevo recurso.
        // Incluye en el cuerpo de la respuesta el objeto datosRespuesta,
        // que contiene los datos estructurados del usuario recién creado.
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    @GetMapping
    //Devuelve una page del objeto datoslistadoUsuario, son 10 los elementos que muestra mediante pageable
    public ResponseEntity<Page<DatosListadoUsuario>> listar(@PageableDefault(size = 10) Pageable paginacion) {
        // Llama al metodo findall del repositorio el cual devuelve una page de usuario
        //el map convierte cada entidad de usuario en un objeto datoslistadoUsuario
        return ResponseEntity.ok(usuarioRepository.findAll(paginacion).map(DatosListadoUsuario::new));
    }

    @GetMapping("/{id}")
    //Devuelve el objeto datosrespuestaUsuarioId del cual se obtiene el id por parametro
    //parametro el cual servira para el uso del dto
    public ResponseEntity<DatosRespuestaUsuarioId> retornaDatos(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaUsuarioId(usuario));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaUsuario> actualizar(@RequestBody DatosActualizarUsuario datosActualizar) {
        Usuario usuario = usuarioRepository.getReferenceById(datosActualizar.id());
        usuario.actualizarDatos(datosActualizar);
        return ResponseEntity.ok( new DatosRespuestaUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }


}
