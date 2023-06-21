package mx.com.cuh.global.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.repository.TbPersonRepository;

@Service
public class UsuarioImpl implements Usuario {
	@Autowired
	private TbPersonRepository tbPersonRepository;
	
	@Override
	public Respuesta <TbPerson> obtenerPersonas(){
		Respuesta<TbPerson> response = new Respuesta <TbPerson>();
		response.setListasPersona(tbPersonRepository.findAll());
		response.setMensaje("ok");
		return response;
	}
	
    @Override
    public  Respuesta<String> borrarPersona(Long id) {
    	Optional<TbPerson> persona = 
    			tbPersonRepository.findById(id); 
    	 Respuesta<String> response = new Respuesta<>();
    	
    	String mensaje = (persona.isPresent()) ? "Se eliminó correctamente" : "El usuario " + id + " no exixte";
    	
    	tbPersonRepository.deleteById(id);
    	response.setMensaje(mensaje);
		
        return response;
        
        
    }
    
	@Override
	public Respuesta<String> insertarPersona(PersonaDTO persona) {
		Long idPersonMaximo =tbPersonRepository.obtenerMaximoIdPerson();
		
		TbPerson personaFinal = new TbPerson();
		personaFinal.setId(idPersonMaximo);
		personaFinal.setNombre(persona.getNombre());
		personaFinal.setEdad(persona.getEdad());
		personaFinal.setSexo(persona.getSexo());

		//Insert into person(id_person,login) values (?,?)
		tbPersonRepository.save(personaFinal);
		Respuesta<String> response = new Respuesta<>();
		response.setMensaje("Se insertó correctamente");
		return response;
	}
}
