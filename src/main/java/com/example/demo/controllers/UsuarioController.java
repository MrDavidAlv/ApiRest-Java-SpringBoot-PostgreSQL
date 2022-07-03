package com.example.demo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.UsuarioModel;
import com.example.demo.services.UsuarioService;

@RestController //clase tipo controlador
@RequestMapping("/usuario")

public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping() //permite que otro cliente ejecute y obtenga los usuarios
    public ArrayList<UsuarioModel> obtenerUsuarios(){
            return usuarioService.obtenerUsuarios();
    }

    @PostMapping()
    public UsuarioModel guardarUusuario(@RequestBody UsuarioModel usuario){   //Recibe como parametro un RequestBody   los clientes puede enviar informacion en el cuerpo de la solicitud del cuerpo Http
        return this.usuarioService.guardarUsuario(usuario);

    }
    
}
