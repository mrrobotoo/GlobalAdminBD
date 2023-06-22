package mx.com.cuh.global.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;
@Service

public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPersonasRepository;

	@Override
	public Respuesta<String> actualizarPersona(long idPerson, PersonasDTO personasDTO) {
		long idUser = idPerson;
		Optional<TbPersonas> persona = tbPersonasRepository.findById(idUser);
		Respuesta<String> response = new Respuesta<>();

		if (persona.isPresent()) {
			TbPersonas personaExistente = persona.get();
			personaExistente.setName(personasDTO.getName());
			personaExistente.setAge(personasDTO.getAge());
			personaExistente.setSex(personasDTO.getSex());

			tbPersonasRepository.save(personaExistente);

			response.setMensaje("Se actualizó correctamente");
		} else {
			response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
		}

		return response;
	}

	
	
	

}
