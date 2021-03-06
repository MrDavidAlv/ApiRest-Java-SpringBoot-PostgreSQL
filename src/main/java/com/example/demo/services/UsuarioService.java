package com.example.demo.services;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.UsuarioModel;
import com.example.demo.repositories.UsuarioRepository;

@Service //Clase de tipo servicio
public class UsuarioService {

    @Autowired //Se usa para no tener que instanciar el repositorio
    UsuarioRepository usuarioRepository;

    public ArrayList<UsuarioModel> obtenerUsuarios(){
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();  //Obtiene los datos y se castea en un ArrayModel para regresarlo en un JSON
    }
    
    public UsuarioModel guardarUsuario(UsuarioModel usuario){  //Regresa la misma informacion pero con el Id ya asignado
        return usuarioRepository.save(usuario);
    }
}
