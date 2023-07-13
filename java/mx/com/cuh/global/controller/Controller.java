package mx.com.cuh.global.controller;

import java.util.List;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 

import mx.com.cuh.global.entity.Tbpersonas;
import mx.com.cuh.global.service.usuario;
import mx.com.cuh.globlal.dto.PersonasDTO;
import mx.com.cuh.globlal.dto.Respuesta;

@org.springframework.stereotype.Controller

public class Controller {
	
	@Autowired
    private usuario usuario;

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/Inicio")
	public String inicio(Model model) {
		List<Tbpersonas>listaPersonas =
				usuario.obtenerpersona().getListasPersona();
		model.addAttribute("listaPersonas",listaPersonas);
		return "inicio";
	}
	
	@PostMapping(value = "/saveperson")
    public String insertarPersona(
            @ModelAttribute PersonasDTO personas) {
         usuario.insertarPersona(personas);
        return "user";
	}
        
    @GetMapping("/borrarPersona/{id}")
    public String Eliminar(@PathVariable
		Long id) {
	 usuario.borrarPersona(id);
	 
    	return "redirect:/Inicio";
    
    }
    
    @PostMapping(value = "/actualizarpersona/{id}")
    public String listo(@PathVariable Long id, 
    	@ModelAttribute PersonasDTO personas) {
    	usuario.actualizarPersona(id, personas);
        return "redirect:/Inicio";
    }
    
}
