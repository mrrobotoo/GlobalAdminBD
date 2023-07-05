package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mx.com.cuh.global.service.usuario;
import mx.com.cuh.globlal.dto.PersonasDTO;
import mx.com.cuh.globlal.dto.Respuesta;

@org.springframework.stereotype.Controller

public class Controller {
	
	@Autowired
    private usuario usuario;

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping(value = "/saveperson")
    public String insertarPersona(
            @ModelAttribute PersonasDTO personas) {
        usuario.insertarPersona(personas);
        return "user";
    }
}
