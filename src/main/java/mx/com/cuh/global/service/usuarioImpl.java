package mx.com.cuh.global.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.entity.Tbpersonas;
import mx.com.cuh.global.repository.TbpersonasRepository;
import mx.com.cuh.globlal.dto.Respuesta;
@Service
public class usuarioImpl implements usuario {
	@Autowired
	private TbpersonasRepository tbpersonasRepository;
	 @Override
	    public  Respuesta<String>borrarPersona(Long id) {
	        Optional<Tbpersonas> persona = 
	                tbpersonasRepository.findById(id); 
	         Respuesta<String> response = new Respuesta<>();
	        String mensaje = (persona.isPresent()) ? "Se eliminó correctamente" : "El usuario " + id + " no existe";
	        tbpersonasRepository.deleteById(id);
	        response.setMensajes(mensaje);
	        return response;
	 }
}
