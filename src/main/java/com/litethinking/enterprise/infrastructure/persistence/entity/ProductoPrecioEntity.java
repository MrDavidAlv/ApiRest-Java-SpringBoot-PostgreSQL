package com.litethinking.enterprise.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "producto_precios",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_codigo", "moneda_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPrecioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_codigo", nullable = false)
    private ProductoEntity producto;

    @Column(name = "moneda_id", nullable = false)
    private Integer monedaId;

    @Column(name = "precio", nullable = false, precision = 15, scale = 2)
    private BigDecimal precio;
}
