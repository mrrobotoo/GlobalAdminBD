package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import mx.com.cuh.global.service.usuario;
import mx.com.cuh.globlal.dto.Respuesta;
@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired 
	private usuario usuario;
	@DeleteMapping(value = "/borrarPersona")
    public Respuesta<String> borrarPersonas(@RequestParam Long id) {
        return usuario.borrarPersona(id);
    }

}
	

