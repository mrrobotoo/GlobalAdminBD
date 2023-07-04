package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller

public class Controller {
	@Autowired
	private User user;

	@RequestMapping("/")
	public String index() {
			return "index";
	}
	@PostMapping(value = "/Saveperson")
	public Respuesta<String> insertarPersona (
			@RequestParam PersonasDTO persona) {
		return user.insertaPersona(persona);
	}
}
//F5 Y F8 
