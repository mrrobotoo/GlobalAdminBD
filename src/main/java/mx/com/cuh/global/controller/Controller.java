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
	private Usuario usuario;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@GetMapping("/inicio")
	public String inicio(Model model){
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersonas", listaPersonas);
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
	//@PostMapping(value = "/actualizar", method=RequestMethod.POST)
	//public String actualizarPersona( @Valid Usuario usuario, BindingResult bindi
		//	if (bindingResult.hasErrors()) {
				//mp.put"usuarios", uc.findAll());
	//return "redirect:/inicio";
			//}
	//Usuario user = uc.findOne(usuario.getId());
	//Usuario.setNombre(usuario.getNombre());
	//Usuario.setNombre(usuario.getNombre());
	//Usuario.setNombre(usuario.getNombre());
	//uc.save(user);
	//mp.put("usuario", user);
	//return "redirect:/inicio";
	
	
	//@PostMapping(value = "/actualizar/{id}", method=RequestMethod.POST)
	//public String actualizarPersona( @Valid ) 
	
    }
	
	
	


	


