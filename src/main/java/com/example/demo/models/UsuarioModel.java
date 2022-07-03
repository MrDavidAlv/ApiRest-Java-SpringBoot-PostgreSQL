package com.example.demo.models;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "usuario") //Creamos la tabla 
public class UsuarioModel {

    //Variables
    @Id //Es un Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private Integer prioridad;
    
    //Getters and Setters

    //Prioridad
    public void setPrioridad(Integer prioridad){
        this.prioridad = prioridad;
    }
    public Integer getPrioridad(){
        return prioridad;
    }

    //Nombre
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public String getNombre(){
        return nombre;
    }

    //Email
    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }


    //Id
    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

}
