package com.litethinking.enterprise.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "excepciones_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcepcionSistemaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "clase_error", length = 255)
    private String claseError;

    @Column(name = "mensaje_error", columnDefinition = "TEXT")
    private String mensajeError;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "endpoint_url", length = 255)
    private String endpointUrl;

    @Column(name = "metodo_http", length = 10)
    private String metodoHttp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametros_entrada", columnDefinition = "jsonb")
    private Map<String, Object> parametrosEntrada;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "estado_resolucion", length = 20)
    private String estadoResolucion;

    @PrePersist
    protected void onCreate() {
        fechaHora = LocalDateTime.now();
        if (estadoResolucion == null) {
            estadoResolucion = "PENDIENTE";
        }
    }
}
