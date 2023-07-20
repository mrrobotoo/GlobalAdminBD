package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller


public class Controller {

	@Autowired
	private User servicio;

/*----------Lista de Usuarios-----------
	@GetMapping({ "/personas", "/" })
	public String listaDePersonas(Model modelo) {
		modelo.addAttribute("personas", servicio.listaDeTodasLasPersonas());
		return "personas";
	}*/
/*----------Lista de Usuarios VERSION 2-----------*/
	@GetMapping({ "/personas", "/", "inicio" })
	public String listaDePersonas(Model modelo, @RequestParam(defaultValue = "0") int page) {
	    if (page < 0) {
	        return "redirect:/inicio";
	    }

	    int pageSize = 10;
	    PageRequest pageRequest = PageRequest.of(page, pageSize);
	    Page<TbPersonas> personasPage = servicio.obtenerPaginaDeLasPersonas(pageRequest);

	    int totalPages = personasPage.getTotalPages();
	    int startPageIndex = Math.max(0, page - 1);
	    int endPageIndex = Math.min(totalPages - 1, page + 1);

	    modelo.addAttribute("personas", personasPage.getContent());
	    modelo.addAttribute("currentPage", page);
	    modelo.addAttribute("totalPages", personasPage.getTotalPages());
	    modelo.addAttribute("startPageIndex", startPageIndex);
	    modelo.addAttribute("endPageIndex", endPageIndex);

	    return "personas"; // O también podrías cambiarlo a "inicio" si deseas mantener la misma vista.
	}


	

/*----------Mostrar Usuarios-----------*/
	@GetMapping("/personas/nuevo")
	public String mostrarPersonasRegistradas(Model modelo) {
		TbPersonas personas = new TbPersonas();
		modelo.addAttribute("personas", personas); 
		return "crear_personas";
	}

/*----------Guardar Usuarios-----------*/
	@PostMapping("/personas")
	public String guardarPersonas(@ModelAttribute("personas") TbPersonas personas) {
		servicio.guardarPersonas(personas);
		return "redirect:/personas";
	}
	
/*----------Editar Usuario-----------*/
	/*CODIGO ORIGINAL*/
	@GetMapping("/personas/editar/{id}")
	public String mostrarPersonasAEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("personas", servicio.obtenerPersonas(id)); 
		return "/editar_personas";
	}

/*----------Actualizar usuario-----------*/
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

/*----------Eliminar Usuario-----------*/
	@GetMapping("/personas/{id}")
	public String eliminarPersonas(@PathVariable Long id) {
		servicio.eliminarPersonas(id);
		return "redirect:/personas";
	}


/*----------PAGINACION-----------
	@GetMapping("inicio")
	public String inicio(Model modelo, @RequestParam(defaultValue = "0")int page) {
		if (page < 0) {
			return "redirect:/incio";
		}
		int pageSize = 10;
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		Page<TbPersonas> personasPage = servicio.obtenerPaginaDeLasPersonas(pageRequest);
		 int totalPages = personasPage.getTotalPages();
	     // Calcular el rango de páginas a mostrar (actual, anterior, siguiente)
	     int startPageIndex = Math.max(0, page - 1);
	     int endPageIndex = Math.min(totalPages - 1, page + 1);   
	     modelo.addAttribute("listaPersonas", personasPage.getContent());
	     modelo.addAttribute("currentPage", page);
	     modelo.addAttribute("totalPages", personasPage.getTotalPages());
	     modelo.addAttribute("startPageIndex", startPageIndex);
	     modelo.addAttribute("endPageIndex", endPageIndex);

	     return "inicio";
	}*/


}
   
