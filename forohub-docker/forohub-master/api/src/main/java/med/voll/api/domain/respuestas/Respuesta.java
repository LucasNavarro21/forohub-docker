package med.voll.api.domain.respuestas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.topicos.Estado;
import med.voll.api.domain.topicos.Topico;
import med.voll.api.domain.usuarios.Usuario;

import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "respuestas")
@Entity(name = "Respuesta")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private Boolean solucion = false;

    public Respuesta(DatosRegistroRespuesta datos, Topico topico, Usuario autor) {
        this.mensaje = datos.mensaje();
        this.topico = topico;
        this.autor = autor;
        this.solucion = datos.solucion();
        if (datos.solucion()) {
            this.topico.setEstado(Estado.SOLUCIONADO);
        } else {
            this.topico.setEstado(Estado.NO_SOLUCIONADO);
        }
    }

    public void actualizarDatos(DatosActualizarRespuesta datosActualizar, Topico topico, Usuario autor) {
        if (datosActualizar.mensaje() != null) {
            this.mensaje = datosActualizar.mensaje();
        }
        if (topico != null) {
            this.topico = topico;
        }
        if (autor != null) {
            this.autor = autor;
        }
        if (datosActualizar.solucion() != this.solucion) {
            this.solucion = datosActualizar.solucion();
        }
    }
}
