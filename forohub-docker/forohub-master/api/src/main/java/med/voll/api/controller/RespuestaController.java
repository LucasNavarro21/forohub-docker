package med.voll.api.controller;

import med.voll.api.domain.respuestas.*;
import med.voll.api.domain.topicos.Estado;
import med.voll.api.domain.topicos.Topico;
import med.voll.api.domain.topicos.TopicoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import med.voll.api.domain.cursos.CursoRepository;


import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;



    @PostMapping
    //DatosRegistroRespuesta son los datos que pedira el json para insertar los datos a la bd
    //DatosRetornoRespuesta son los datos que vera el cliente una vez que se haya completado la solicitud
    public ResponseEntity<DatosRetornoRespuesta> registrar(@RequestBody DatosRegistroRespuesta datosRegistro, UriComponentsBuilder uri) {
        Topico topico = topicoRepository.getReferenceById(datosRegistro.topicoId());
        if (topico.getEstado() == Estado.CERRADO) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Usuario autor = usuarioRepository.getReferenceById(datosRegistro.autorId());
        Respuesta respuesta = respuestaRepository.save(new Respuesta(datosRegistro, topico, autor));
        topico.agregarRespuesta(respuesta);
        DatosRetornoRespuesta datosRespuesta = new DatosRetornoRespuesta(respuesta);
        URI url = uri.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listar(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(respuestaRepository.findAll(paginacion).map(DatosListadoRespuesta::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRetornoRespuestaId> retornaDatos(@PathVariable Long id) {
        Respuesta respuesta= respuestaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRetornoRespuestaId(respuesta));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRetornoRespuesta> actualizar(@RequestBody DatosActualizarRespuesta datosActualizar) {
        Respuesta respuesta = respuestaRepository.getReferenceById(datosActualizar.id());
        Usuario autor = usuarioRepository.getReferenceById(datosActualizar.autorId());
        Topico topico = topicoRepository.getReferenceById(datosActualizar.id());
        respuesta.actualizarDatos(datosActualizar, topico, autor);
        return ResponseEntity.ok( new DatosRetornoRespuesta(respuesta));
    }

}
