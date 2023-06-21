package mx.com.cuh.global.service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;

public interface Usuario {
	
	Respuesta<TbPerson> obtenerPersonas();
	
	Respuesta<String> borrarPersona(Long id);

}
