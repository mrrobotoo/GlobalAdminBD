package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mx.com.cuh.global.service.EstudianteServicio;

@Controller
public class Estudianteontrolador {
	@Autowired
	private EstudianteServicio servicio;
	
	@GetMapping("/")
	public String listarEstudiantes(Model modelo) {
		modelo.addAttribute("estudiante",servicio.listarTodosLosEstudiantes());
		return "estudiantes";
	}
	

}
