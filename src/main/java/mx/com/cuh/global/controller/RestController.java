package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	private User user;
	
    @GetMapping(value = "/persona")
    public Respuesta<TbPersonas> obtenerRegistros() {
        return user.obtenerRegistros();
    }
    
    
    @PostMapping(value = "/insertarPersonas")
    public Respuesta insertarPersona(
            @RequestBody PersonasDTO persona) {
        return user.insertarPersona(persona);
        
    }

}
