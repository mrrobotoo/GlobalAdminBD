package mx.com.cuh.global.service;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;

public interface User {

<<<<<<< HEAD
	Respuesta<String> borrarPersona(Long idUser);
	Respuesta<String> insertaPersona(PersonasDTO persona);
=======
	Respuesta<TbPersonas> obtenerRegistros();

	Respuesta insertarPersona(PersonasDTO persona);

	Respuesta<String> borrar(Long idPerson);

	Respuesta<String> actualizarPersona(long idPerson, PersonasDTO personasDTO);


>>>>>>> origin/Jonathan_Emmanuel_Cruz_Altamirano
}
