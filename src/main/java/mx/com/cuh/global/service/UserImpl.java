package mx.com.cuh.global.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;


@Service
public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPbersonasRepository;
	
@Override
  	public Respuesta<String> borrarPersona(Long IdPerson){
	Optional<TbPersonas> persona =
			tbPbersonasRepository.findById(IdPerson);
	Respuesta<String> response = new Respuesta<>();
	
	String mensaje =(persona.isPresent()) ? "Se eliminó correctamente" : "El usuario " + IdPerson + " no existe";
	
	tbPbersonasRepository.deleteById(IdPerson);
	response.setMensaje(mensaje);
	
	return response;
}

}
