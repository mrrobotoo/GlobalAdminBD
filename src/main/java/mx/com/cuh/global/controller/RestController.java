package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Autowired
	private Usuario usuario;

	@GetMapping(value = "/person")
	public Respuesta<TbPerson> listaPersonas() {
		return usuario.obtenerPersonas();
	}

	@PostMapping(value = "/person")
	public Respuesta insertarPersonas(@RequestBody PersonaDTO persona) {
		return usuario.insertarPersona(persona);
	}

	@DeleteMapping(value = "/person")
	public Respuesta borrarPersonas(@RequestParam Long id) {
		return usuario.borrarPersona(id);
	}

	@PutMapping(value = "/person", params = "id")
	public Respuesta actualizarpersonas(@RequestParam Long id, @RequestBody PersonaDTO persona) {
	    return usuario.actualizarPersona(id, persona);
	}
}

