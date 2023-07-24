package mx.com.cuh.global.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;

public interface User {

	Respuesta<TbPersonas> obtenerRegistros();

	Respuesta insertarPersona(PersonasDTO persona);

	Respuesta<String> borrar(Long idPerson);

	Respuesta<String> actualizarPersona(long idPerson, PersonasDTO personasDTO);
	
	Page<TbPersonas> obtenerRegistroPaginados(Pageable pageable);

	List<TbPersonas> obtenerTodosLosRegistros();

}