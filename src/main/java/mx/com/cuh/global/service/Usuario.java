package mx.com.cuh.global.service;

import mx.com.cuh.global.dto.Respuesta;

public interface Usuario {
	Respuesta<String> borrarPersona(Long id);
}
