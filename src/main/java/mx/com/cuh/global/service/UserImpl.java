package mx.com.cuh.global.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;


@Service
public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPbersonasRepository;
	
@Override
  	public Respuesta<String> borrarPersona(Long idUser){
	Optional<TbPersonas> persona =
			tbPbersonasRepository.findById(idUser);
	Respuesta<String> response = new Respuesta<>();
	
	String mensaje =(persona.isPresent()) ? "Se eliminó correctamente" : "El usuario con ID " + idUser + " no existe, favor de validar";
	
	tbPbersonasRepository.deleteById(idUser);
	response.setMensaje(mensaje);
	
	return response;
}

@Override
public Respuesta<String> insertaPersona(PersonasDTO persona) {
	// TODO Auto-generated method stub
	return null;
}

}

