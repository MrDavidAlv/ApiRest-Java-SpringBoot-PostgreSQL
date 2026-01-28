package com.litethinking.enterprise.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoOrdenEntity estado;

    @Column(name = "fecha_orden", nullable = false, updatable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "usuario_crea_id")
    private Long usuarioCreaId;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrdenDetalleEntity> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaOrden = LocalDateTime.now();
    }

    public void addDetalle(OrdenDetalleEntity detalle) {
        detalles.add(detalle);
        detalle.setOrden(this);
    }

    public void removeDetalle(OrdenDetalleEntity detalle) {
        detalles.remove(detalle);
        detalle.setOrden(null);
    }
}
