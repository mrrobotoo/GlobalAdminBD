package mx.com.cuh.global.service;
import mx.com.cuh.globlal.dto.PersonasDTO;
import mx.com.cuh.globlal.dto.Respuesta;
import mx.com.cuh.global.entity.Tbpersonas;
public interface usuario {

    Respuesta<Tbpersonas> obtenerpersona();
    
	Respuesta<String> insertarPersona(PersonasDTO personas);
	
	Respuesta<String> actualizarPersona(Long id, Tbpersonas personaActualizada);
	
	 Respuesta<String> borrarPersona(Long id);

	Respuesta<String> actualizarPersona(Long id);
    
	Respuesta<String> actualizarPersona(long id, PersonasDTO personasDTO);
	
}
