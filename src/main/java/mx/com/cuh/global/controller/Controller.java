package mx.com.cuh.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
 @GetMapping("/inicio")
 public String inicio() {
	 Respuesta<TbPersonas>cosa=user.obtenerRegistros();
	 List<TbPersonas> superLista = cosa.getListaPersonas();
	 for(TbPersonas persona : superLista) {
		 System.out.print(persona.getAge());
		 
	 }
	 
	 return "inicio";
 }
 
 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "user";
 	}
 }

