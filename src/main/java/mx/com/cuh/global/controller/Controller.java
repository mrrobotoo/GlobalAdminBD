package mx.com.cuh.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;
	
 @RequestMapping("/")
	 public String index() {
		 return "index";
	 }
 
 @GetMapping("inicio")
	 public String inicio(Model model) {
		 List<TbPersonas> listaPersonas= 
		user.obtenerRegistros().getListaPersonas();		 	 
		 model.addAttribute("listaPersonas",listaPersonas);
	 return "inicio";
 	}

 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "user";
 	} 
 
@GetMapping("/eliminar/{idUser}")
	public String eliminar(@PathVariable
        Long idUser) {
     user.borrar(idUser);
		return "redirect:/inicio";
	}
@PostMapping(value = "/actualizar/{idUser}")
public String actualizarPersona(@PathVariable
		Long idUser, 
	@ModelAttribute PersonasDTO persona) {
	user.actualizarPersona(idUser, persona);
    return "redirect:/inicio";
}
 }
