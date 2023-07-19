package mx.com.cuh.global.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import mx.com.cuh.global.dto.PersonaDTO;

import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;

@org.springframework.stereotype.Controller

public class Controller {
	
	@Autowired
	private Usuario usuario;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/inicio")
		public String inicio(Model model, @RequestParam(defaultValue = "0") int page) { //int page: sirve para especificar la página que se va a visualizar.
																						
		    int registrosCount = 2; //Cuantos registros queremos que aparescan por página.
		    Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount)); //Con registrosCount cuenta la cantidad de registros que se especifico anteriormente.
		    																										
		    List<TbPerson> listaPersonas = paginaPersonas.getContent(); 
		    model.addAttribute("listaPersonas", listaPersonas); 
		    model.addAttribute("currentPage", page); //HAce que se visualice la página actual en la que se encuentra.
		    model.addAttribute("totalPages", paginaPersonas.getTotalPages()); //Aparece cuantas páginas tiene.
		    return "inicio"; 
		}
	
	@PostMapping(value = "/saveperson")
	public String insertarPersonas(
			@ModelAttribute PersonaDTO persona) {
		usuario.insertarPersona(persona);
		return "user";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(Model model, @PathVariable Long id){
		usuario.borrarPersona(id);
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersonas", listaPersonas);
		return "redirect:/inicio";
	}
	
	@PostMapping(value = "/actualizar/{id}") //ACTUALIZAR PERSONA
	public String actualizarPersona( @PathVariable Long id, @ModelAttribute PersonaDTO persona){
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		usuario.actualizarPersona(id,persona);
			return "redirect:/inicio";
		} 
	
	
}
			  
	
    
	
	
	


	


