package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.UsuarioModel;

@Repository //Clase de tipo repositorio
public interface UsuarioRepository extends CrudRepository<UsuarioModel, Long>{      //Crea la interfaz e importa el modelo de UsuarioModel
    
}
