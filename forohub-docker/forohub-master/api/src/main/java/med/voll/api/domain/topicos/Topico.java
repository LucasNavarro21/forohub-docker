package med.voll.api.domain.topicos;


import jakarta.persistence.*;
import lombok.*;
import med.voll.api.domain.cursos.Curso;
import med.voll.api.domain.respuestas.Respuesta;
import med.voll.api.domain.usuarios.Usuario;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topicos")
@Entity(name = "Topico")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.NO_RESPONDIDO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso cursos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "topico_id", referencedColumnName = "id")
    private List<Respuesta> respuestas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario usuario;

    public Topico(DatosRegistroTopico datos, Usuario usuario, Curso cursos) {
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.usuario = usuario;
        this.cursos = cursos;
    }

    public void agregarRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
        if (respuesta.getSolucion()) {
            this.estado = Estado.SOLUCIONADO;
        } else {
            this.estado = Estado.NO_SOLUCIONADO;
        }
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizar, Usuario usuario, Curso cursos) {
        if (datosActualizar.titulo() != null) {
            this.titulo = datosActualizar.titulo();
        }
        if (datosActualizar.mensaje() != null) {
            this.mensaje = datosActualizar.mensaje();
        }
        if (datosActualizar.estado() != datosActualizar.estado()) {
            this.estado = datosActualizar.estado();
        }
        if (usuario != null) {
            this.usuario = usuario;
        }
        if (cursos != null) {
            this.cursos = cursos;
        }

    }

    public void cerrarTopico() {
        this.estado = Estado.CERRADO;
    }
}
