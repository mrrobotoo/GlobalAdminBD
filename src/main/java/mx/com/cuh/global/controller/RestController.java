package mx.com.cuh.global.controller;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;

import mx.com.cuh.global.service.User;
import org.springframework.web.bind.annotation.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
	@Autowired
	private User user;

	@PutMapping("/person")
	public Respuesta<String> actualizarPersona(@RequestParam("idPerson") Long idPerson, @RequestBody PersonasDTO person) {


		return user.actualizarPersona(idPerson, person);
	}

}
