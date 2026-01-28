package com.litethinking.enterprise.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orden_detalles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDetalleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable = false)
    private OrdenEntity orden;

    @Column(name = "producto_codigo", nullable = false, length = 50)
    private String productoCodigo;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario_historico", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioUnitarioHistorico;
}
