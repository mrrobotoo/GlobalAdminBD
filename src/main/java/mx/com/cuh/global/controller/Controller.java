package mx.com.cuh.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;
	
 @RequestMapping("/")
	 public String index() {
		 return "index";
	 }
/* 
 @GetMapping("inicio")
	 public String inicio(Model model) {
		 List<TbPersonas> listaPersonas= 
		user.obtenerRegistros().getListaPersonas();		 	 
		 model.addAttribute("listaPersonas",listaPersonas);
	 return "inicio";
 	}*/
 
 @GetMapping("inicio")
 public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
// Obtiene la respuesta de la lista de personas desde el servicio (base de datos)
	 Respuesta<TbPersonas> response = user.obtenerRegistros(); 
// Tamaño de la página, puedes ajustarlo según tus necesidades
     int pageSize = 10; 
// Calcula el número total de páginas basado en el tamaño de la lista de personas dividido por el tamaño de página (pageSize)
     int totalPages = (int) Math.ceil((double) response.getListaPersonas().size() / pageSize); 
// Calcula el índice de inicio para la página actual
     int startIndex = page * pageSize; 
// Calcula el índice de fin para la página actual asegurándose de no exceder el tamaño total de la lista
     int endIndex = Math.min(startIndex + pageSize, response.getListaPersonas().size()); 
// Obtiene la sublista de personas correspondiente a la página actual
     List<TbPersonas> listaPersonas = response.getListaPersonas().subList(startIndex, endIndex); 
     
     model.addAttribute("listaPersonas", listaPersonas); // Agrega la lista de personas para mostrar en la vista
     model.addAttribute("currentPage", page); // Agrega el número de página actual para usar en la paginación en la vista
     model.addAttribute("totalPages", totalPages); // Agrega el número total de páginas para usar en la paginación en la vista

     return "inicio"; // Devuelve el nombre de la vista (plantilla Thymeleaf) a mostrar en el navegador
 }

 
 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "user";
 	} 
 

 @GetMapping("/eliminar/{idUser}")
 public String eliminar(@PathVariable Long idUser, 
		 @RequestParam(defaultValue = "0") int page) {
     // Llamamos al método "borrar" del servicio "user" para eliminar la entidad con el id proporcionado
     user.borrar(idUser);

     // Redirigimos a la misma página de paginación con el número de página actual como parámetro
     return "redirect:/inicio?page=" + page;
 }

@PostMapping(value = "/actualizar/{idUser}")
public String actualizarPersona(@PathVariable Long idUser, 
	@ModelAttribute PersonasDTO persona) {
	user.actualizarPersona(idUser, persona);
    return "redirect:/inicio";
}

 }