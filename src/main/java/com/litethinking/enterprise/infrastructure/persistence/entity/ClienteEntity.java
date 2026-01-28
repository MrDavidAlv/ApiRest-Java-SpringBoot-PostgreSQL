package com.litethinking.enterprise.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "documento", unique = true, nullable = false, length = 20)
    private String documento;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "correo", length = 100)
    private String correo;
}
