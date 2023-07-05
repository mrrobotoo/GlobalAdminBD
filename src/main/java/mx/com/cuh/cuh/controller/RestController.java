package mx.com.cuh.cuh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.cuh.cuh.dto.Respuesta;
import mx.com.cuh.cuh.entity.TbPerson;
import mx.com.cuh.cuh.service.Usuario;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private Usuario usuario;
    @GetMapping(value = "/obtenerpersona")
    public Respuesta<TbPerson> obtenerpersona() {
        return usuario.obtenerpersona();
    }
}




