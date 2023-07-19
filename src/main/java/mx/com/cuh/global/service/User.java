package mx.com.cuh.global.service;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.entity.TbPersonas;

public interface User {

	Respuesta<TbPersonas> obtenerRegistros();
	
	Respuesta<String> borrarPersona(Long id);

	Respuesta<String> insertarPersona(PersonaDTO persona);

	Respuesta<String> actualizarPersona(Long id, PersonasDTO personas);

	Respuesta<String> borrar(Long idUser);

	Respuesta<String> actualizarPersona(long idUser, PersonasDTO personasDTO);

	Respuesta<String> insertarPersona(PersonasDTO persona);
	
	Page<TbPersonas> getAll(Pageable pageable); 


}
