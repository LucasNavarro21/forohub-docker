package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.cursos.Curso;
import med.voll.api.domain.cursos.CursoRepository;
import med.voll.api.domain.topicos.*;
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
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {


    @Autowired
    CursoRepository cursoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrar(@RequestBody @Valid DatosRegistroTopico datosRegistro, UriComponentsBuilder uri) {

//        Asigna en autor el objeto mediante el mapeo de datosregistrotopico buscado por id
        Usuario autor = usuarioRepository.getReferenceById(datosRegistro.autorId());
//        Asigna en curso el objeto mediante el mapeo de datosregistrotopico buscado por id
        Curso curso = cursoRepository.getReferenceById(datosRegistro.cursoId());
        //Guarda el topico mediante un constructor creado en topico con estos datos
        Topico topico = topicoRepository.save(new Topico(datosRegistro, autor, curso));
        //Asigna a datosrespuesta el objeto el cual sera una respuesta formateada mediante los datos obtenido de topico
        DatosRespuestaTopico datosRespuesta = new DatosRespuestaTopico(topico);
        URI url = uri.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listar(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopico::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopicoId> retornaDatos(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaTopicoId(topico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizar(@RequestBody DatosActualizarTopico datosActualizar) {
        Usuario autor = usuarioRepository.getReferenceById(datosActualizar.autorId());
        Curso curso = cursoRepository.getReferenceById(datosActualizar.cursoId());
        Topico topico = topicoRepository.getReferenceById(datosActualizar.id());
        topico.actualizarDatos(datosActualizar, autor, curso);
        return ResponseEntity.ok( new DatosRespuestaTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        //topicoRepository.delete(topico);
        topico.cerrarTopico();
        return ResponseEntity.noContent().build();
    }
}
