package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.service.Usuario;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private Usuario user;
			
	@RequestMapping("/")
	public String index() {
		return"index";
	}
	
	@GetMapping("/inicio")
	public String inicio() {
		/*Respuesta<TbPersonas> cosa =user.obtenerPersonas();
		List<TbPersonas> superLista = cosa.getListasPersona();*/
		user.obtenerPersonas();
		return"inicio";
	}
	@PostMapping(value = "/saveperson")
	public String insertarPersonas(
			@ModelAttribute PersonaDTO persona) {
		user.insertarPersona(persona);
		return "user";
	}
}

