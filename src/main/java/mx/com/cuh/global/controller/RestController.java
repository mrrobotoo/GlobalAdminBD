package mx.com.cuh.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	private User user;
	
    @GetMapping(value = "/person")
    public Respuesta<TbPersonas> obtenerRegistros() {
        return user.obtenerRegistros();
    }
    
    
    @PostMapping(value = "/person")
    public Respuesta insertarPersona(
            @RequestBody PersonasDTO persona) {
        return user.insertarPersona(persona);
    }
    
    @DeleteMapping(value = "/person")
    public Respuesta borrar(@RequestParam 
            Long idUser) {
        return user.borrar(idUser);    
    }
    
    @PutMapping("/person")
    public Respuesta<String> actualizarPersona(@RequestParam("idUser") Long idUser, @RequestBody PersonasDTO person) {

        return user.actualizarPersona(idUser, person);
    }

}
