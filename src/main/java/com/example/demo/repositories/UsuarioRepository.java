package com.example.demo.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.UsuarioModel;

@Repository //Clase de tipo repositorio
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Long>{      //Crea la interfaz e importa el modelo de UsuarioModel
    public abstract ArrayList<UsuarioModel> findByPrioridad(Integer prioridad);     //Para filtrar los usuarios mediante prioridad
}
