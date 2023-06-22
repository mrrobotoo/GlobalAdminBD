package mx.com.cuh.global.service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;

public interface User {

	Respuesta<TbPersonas> obtenerRegistros();

	Respuesta<String> borrarPersona(Long IdPerson);
}
