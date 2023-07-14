package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller


public class Controller {

	@Autowired
	private User servicio;

	@GetMapping({ "/personas", "/" })
	public String listaDePersonas(Model modelo) {
		modelo.addAttribute("personas", servicio.listaDeTodasLasPersonas());
		return "personas";
	}

	@GetMapping("/personas/nuevo")
	public String mostrarPersonasRegistradas(Model modelo) {
		TbPersonas personas = new TbPersonas();
		modelo.addAttribute("personas", personas); 
		return ("crear_personas");
	}

	@PostMapping("/personas")
	public String guardarPersonas(@ModelAttribute("personas") TbPersonas personas) {
		servicio.guardarPersonas(personas);
		return ("redirect:/personas");
	}
/*----------mostrar-----------*/
	@GetMapping("/personas/editar/{id}")
	public String mostrarPersonasAEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("personas", servicio.obtenerPersonas(id));
		return "/editar_personas";
	}

	@PostMapping("/personas/{id}")
	public String actualizarPersona(@PathVariable Long id, @ModelAttribute("personas") TbPersonas personas,
			Model modelo) {
		TbPersonas personaExistente = servicio.obtenerPersonas(id);
		personaExistente.setId(id);
		personaExistente.setNombre(personas.getNombre());
		personaExistente.setEdad(personas.getEdad());
		personaExistente.setSexo(personas.getSexo());

		servicio.actualizarPersonas(personaExistente);
		return "redirect:/personas";
	}

	@GetMapping("/personas/{id}")
	public String eliminarPersonas(@PathVariable Long id) {
		servicio.eliminarPersonas(id);
		return "redirect:/personas";
	}

}
