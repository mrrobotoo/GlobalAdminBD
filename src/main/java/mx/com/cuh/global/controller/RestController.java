package mx.com.cuh.global.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;
import mx.com.cuh.global.dto.Respuesta;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	private Usuario usuario;
	
	//PENDIENTE NO OBTIENE DATA DE DB
	@GetMapping(value = "/person")
	public Respuesta<TbPerson> listaPersonas() {
		return usuario.obtenerPersonas();
	}
}
