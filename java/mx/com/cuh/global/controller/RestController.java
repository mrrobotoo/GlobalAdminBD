package mx.com.cuh.global.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.entity.Tbpersonas;
import mx.com.cuh.global.service.usuario;
import mx.com.cuh.globlal.dto.PersonasDTO;
import mx.com.cuh.globlal.dto.Respuesta;
	@org.springframework.web.bind.annotation.RestController
	public class RestController {
	    @Autowired
	    private usuario usuario;
	    @GetMapping(value = "/persona")
	    public Respuesta<Tbpersonas> obtenerpersona() {
	        return usuario.obtenerpersona();
	    }
	    @PostMapping(value = "/insertarPersona")
	    public Respuesta insertarPersona(
	            @RequestBody PersonasDTO personas) {
	        return usuario.insertarPersona(personas);
	    }
	    
	    
	    @DeleteMapping(value = "/borrarPersona")
		public Respuesta<String> borrarPersona(@RequestParam 
				Long id) {
			return usuario.borrarPersona(id);
	 
	}
	    @PutMapping(value = "/actualizarpersona")
	    public Respuesta<String> actualizarPersonas(@RequestParam Long id, @RequestBody Tbpersonas personaActualizada) {
	        return usuario.actualizarPersona(id, personaActualizada); 
	    }
	    

	}
	

