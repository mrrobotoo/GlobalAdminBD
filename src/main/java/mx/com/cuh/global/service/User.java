package mx.com.cuh.global.service;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;

public interface User {

	Respuesta<String> borrarPersona(Long idUser);
	Respuesta<String> insertaPersona(PersonasDTO persona);
}
