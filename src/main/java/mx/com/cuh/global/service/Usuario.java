package mx.com.cuh.global.service;

import org.springframework.data.domain.Page; //NUEVAS LIBRERIAS
import org.springframework.data.domain.PageRequest; //NUEVA LIBRERÍA

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson; //SE IMPORTAN LIBRERIAS

public interface Usuario {

	Respuesta<TbPerson> obtenerPersonas();

	Respuesta<String> borrarPersona(Long id);

	Respuesta<String> insertarPersona(PersonaDTO persona);

	Respuesta<String> actualizarPersona(Long id, PersonaDTO persona);

	Page<TbPerson> obtenerPersonasPorPagina(PageRequest pageRequest); //TOMA EL OBJETO "PageRequest" COMO PARAMETRO Y REPRESENTA LA CONFIGURACIÓN
																		// DE LAS PÁGINAS, COMO SU NUMERO DE PÁGINA Y TAMAÑO DE REGISTROS DENTRO DE ELLA.
																		// FINALMENTE, DEVUELVE UNA INSTANCIA QUE CONTIENE LA PÁGINA CON LOS REGISTROS

	Page<TbPerson> obtenerPersonasPorNombre(String nombre, PageRequest pageRequest); //FILTRO

}
