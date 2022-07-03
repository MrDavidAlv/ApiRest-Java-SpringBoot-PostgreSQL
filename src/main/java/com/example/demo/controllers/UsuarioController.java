package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping( path = "/{id}")
    public Optional<UsuarioModel> obtenerUsuarioPorId(@PathVariable("id") Long id){
        return this.usuarioService.obtenerPorId(id);
    }

    @GetMapping( path = "/query")
    public ArrayList<UsuarioModel> obtenerUsuarioPorPrioridad(@RequestParam("prioridad") Integer prioridad){
        return this.usuarioService.obtenerPorPrioridad(prioridad);
    }
    @DeleteMapping("/{id}")
    public String eliminarPortId(@PathVariable("id") Long id){
        boolean ok = this.usuarioService.eliminarUsuario(id);
        if(ok){
            return "Se elimino el usuario con id: " + id;
        }else{
            return "No se pudo eliminar el usuario con id: " + id;
        }
    }
}
