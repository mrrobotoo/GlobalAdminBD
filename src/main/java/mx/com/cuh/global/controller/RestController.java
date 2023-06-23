package mx.com.cuh.global.controller;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.repository.TbPersonRepository;
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

	@PostMapping(value = "/person")
	public Respuesta insertarPersonas(
			@RequestBody PersonaDTO persona) {
		return usuario.insertarPersona(persona);

	}
	
	@DeleteMapping(value = "/borrarPersona")
	public Respuesta<String> borrarPersonas(@RequestParam Long id) {
		return usuario.borrarPersona(id);
		
	}

	@PutMapping(value = "/actperson")
	public Respuesta<String> actualizarPersonas(@RequestParam 
			Long id, PersonaDTO persona ) {
			return usuario.actualizarPersona(id); 

	}

}
	




 
