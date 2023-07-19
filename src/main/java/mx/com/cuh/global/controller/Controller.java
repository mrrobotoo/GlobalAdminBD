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

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@org.springframework.stereotype.Controller

public class Controller {
	
	@Autowired
	private Usuario usuario;
	
	@RequestMapping("/index") //INICIO / INDEX
	public String index() {
		return "index";
	}
	
	//SE MODIFICÓ PARA LA IMPLEMENTACIÓN DEL PAGINADOR
	@GetMapping("/inicio")
	public String inicio(Model model, @RequestParam(defaultValue = "0") int page) { //MÉTODO QUE TOMA DOS PARAMETROS (MODEL) QUE TOMA DATOS AL MODELO PARA SU REPRESENTACIÓN GRÁFICA.
																					//EL VALOR POR DEFAULT SERÁ DE CERO
																					//"INT PAGE" ES UN PARAMETRO DE CONSULTA PARA ESPECIFICAR LA PÁGINA QUE SE VAMOS A CONSULTAR
	    int registrosCount = 10; //VARIABLE QUE ALMACENA LA CANTIDAD DE REGISTROS POR PÁGINA
	    Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount)); //MANDAMOS LLAMAR A NUESTRO MÉTODO 'ObtenerPersonasPorPagina' QUE IMPLEMENTAMOS EN 'Usuario.java'
	    																										//UTILIZAMOS 'PageRequest' PARA ESPECIFICAR LA PÁGINA A LA QUE VAMOS A ENTRAR (USAMOS 'page' PARA ESPECIFICAR ESTO
	    																										//FINALMENTE, USAMOS 'registrosCount' PARA ESPECIFICAR LA CANTIDAD DE REGISTROS POR PÁGINA.
	    List<TbPerson> listaPersonas = paginaPersonas.getContent(); //OBTIENE LISTA DE USUARIOS REQUERIDOS
	    model.addAttribute("listaPersonas", listaPersonas); //SE AGREGA LISTA DE USUARIOS AL MODELO DE VISTA PARA QUE EL USUARIO PUEDA VERLO
	    model.addAttribute("currentPage", page); //MUESTRA LA PÁGINA ACTUAL EN LA QUE SE ENCUENTRA EL USUARIO
	    model.addAttribute("totalPages", paginaPersonas.getTotalPages()); //PERMITE QUE LA VISTA MUESTRE EL TOTAL DE PÁGINAS DISPONIBLES EN EL PÁGINADOR
	    return "inicio"; //RETORNA LA LISTAS DE PERSONAS
	}
	
	@PostMapping(value = "/saveperson") //INSERTAR PERSONA
	public String insertarPersonas(
			@ModelAttribute PersonaDTO persona) {
		usuario.insertarPersona(persona);
		return "user";
	}
	
	@GetMapping("/eliminar/{id}") //ELIMINAR PERSONA
	public String eliminar(Model model, @PathVariable Long id){
		usuario.borrarPersona(id);
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersonas", listaPersonas);
		return "redirect:/inicio";
	}
	
	@PostMapping(value = "/actualizar/{id}") //ACTUALIZAR PERSONA
	public String actualizarPersona(@PathVariable("id") Long id, @ModelAttribute PersonaDTO persona) {
		usuario.actualizarPersona(id, persona);
		return "redirect:/inicio";
	}
	
}
