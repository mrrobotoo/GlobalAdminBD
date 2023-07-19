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
 
/* @GetMapping("inicio")
	 public String inicio(Model model) {
		 List<TbPersonas> listaPersonas= 
		user.obtenerRegistros().getListaPersonas();		 	 
		 model.addAttribute("listaPersonas",listaPersonas);
	 return "inicio";
 	}*/

 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "user";
 	} 
 
 @GetMapping("inicio")
 public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
     if (page < 0) {
            return "redirect:/inicio";
        }
     int pageSize = 10;
     PageRequest pageRequest = PageRequest.of(page, pageSize);
     Page<TbPersonas> personasPage = user.obtenerRegistroPaginados(pageRequest);
     int totalPages = personasPage.getTotalPages();
     if (page >= totalPages) {
         return "redirect:/inicio?page=" + (totalPages - 1);
     }
     model.addAttribute("listaPersonas", personasPage.getContent());
     model.addAttribute("currentPage", page);
     model.addAttribute("totalPages", personasPage.getTotalPages());
     return "inicio";
 }

@PostMapping(value = "/actualizar/{idUser}")
public String actualizarPersona(@PathVariable
		Long idUser, 
	@ModelAttribute PersonasDTO persona) {
	user.actualizarPersona(idUser, persona);
    return "redirect:/inicio";
}
 }
