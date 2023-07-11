package mx.com.cuh.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;

@org.springframework.stereotype.Controller
public class Controller {
	
	@Autowired
	private Usuario user;
	
	
	@RequestMapping("/")
	public String index(){
		return "index";
	}
	
	@GetMapping("/inicio")
	public String inicio(Model model){
		List<TbPerson>listaPersona = 
				user.obtenerPersonas().getListasPersona();
		
		/*Es una forma de que se valla agregando solo*/
		/*Respuesta<TbPerson> cosa =user.obtenerPersonas();
		List<TbPerson>superlista = cosa.getListaPersonas();
		for(TbPerson persona :superlista){
			System.out println(persona.getAge());
		}*/
		model.addAttribute("listaPersona",listaPersona);
		
		return "Inicio";
	}
	
	
	
	@PostMapping(value = "/saveperson")
	public String insertarPersonas(
			@ModelAttribute PersonaDTO persona) {
		user.insertarPersona(persona);
		return "user";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable
		Long id/*, Model model*/) { /*Es una forma diferente para hacer que se visualice nuevamente a tabla pero posteriormente 
		aparecerá un error, así que no es el método más correcto*/
		user.borrarPersona(id);
		
		/*List<TbPerson>listaPersona = 
				user.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersona",listaPersona);*/
	
	return "redirect:/Inicio";
	}
		

}
