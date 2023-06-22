package mx.com.cuh.global.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.repository.TbPersonaRepository;

@Service
public class UsuarioImpl implements Usuario{
	@Autowired
	private TbPersonaRepository tbPersonaRepository;
	@Override
	public Respuesta<String> borrarPersona(Long id) {
		Optional<TbPerson> persona = tbPersonaRepository.findById(id);
		
		Respuesta<String> response = new Respuesta<>();
		
		String mensaje = (persona.isPresent()) ? "Se elimino correctamente" : "El usuario" 
			+ id + "NO EXISTE";
		
		tbPersonaRepository.deleteById(id);
		response.setMensaje(mensaje);
		return response;
	}
}
