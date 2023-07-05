package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;
	
	@RequestMapping("/")    
	public String index(){    
		return"index";    
	} 
	
	@GetMapping("/inicio")    
	public String inicio(){    
		return"user";    
	} 
	@PostMapping(value = "/saveperson")
    public String insertarPersona(
            @ModelAttribute PersonasDTO persona) {
		 user.insertarPersona(persona);
        return "user";
    }
	
	
	
	
	
//	@RequestMapping(value="/save", method=RequestMethod.POST)    
//	public ModelAndView save(@ModelAttribute User user) {    
//	ModelAndView modelAndView = new ModelAndView();    
//	modelAndView.setViewName("user-data");        
//	modelAndView.addObject("user", user);      
//	return modelAndView;    
//	}    
}
