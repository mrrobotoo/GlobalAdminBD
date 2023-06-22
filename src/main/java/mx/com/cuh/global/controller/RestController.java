package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;


import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.service.Usuario;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	private Usuario usuario;
	
	@DeleteMapping(value = "/borrarPersona")
	public Respuesta<String> borrarPersona(@RequestParam 
			Long id) {
		return usuario.borrarPersona(id);	
	}
}

