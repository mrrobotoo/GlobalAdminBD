package mx.com.cuh.global.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.com.cuh.globlal.dto.PersonasDTO;
import mx.com.cuh.globlal.dto.Respuesta;
import mx.com.cuh.global.entity.Tbpersonas;
import mx.com.cuh.global.repository.TbpersonasRepository;

@Service
public class usuarioImpl implements usuario{
	@Autowired
	private TbpersonasRepository tbpersonaRepository;

@Override
public Respuesta <Tbpersonas> obtenerpersona(){
    Respuesta<Tbpersonas> response = new Respuesta <Tbpersonas>();
    response.setListasPersona(tbpersonaRepository.findAll());
    response.setMensaje("correcto");

    return response;
    
	}
@Override
public Respuesta<String> insertarPersona(PersonasDTO personas) {
    Long id =tbpersonaRepository.obtenerMaximoid();
    Tbpersonas NuevoRegistro = new Tbpersonas();
    NuevoRegistro.setId(id);
    NuevoRegistro.setNombre(personas.getNombre());
    NuevoRegistro.setEdad(personas.getEdad());
    NuevoRegistro.setSexo(personas.getSexo());
    tbpersonaRepository.save(NuevoRegistro);
    Respuesta<String> response = new Respuesta<>();
    response.setMensaje("Se insertó correctamente");
    return response;
}
public Respuesta<String> actualizarPersona(Long id, Tbpersonas personaActualizada) {
    Optional<Tbpersonas> personaExistente = tbpersonaRepository.findById(id); 
    Respuesta<String> response = new Respuesta<>();
    
    if (personaExistente.isPresent()) {
        Tbpersonas persona = personaExistente.get();
        
        persona.setEdad(personaActualizada.getEdad());
        persona.setNombre(personaActualizada.getNombre());
        persona.setSexo(personaActualizada.getSexo());
        tbpersonaRepository.save(persona);
        
        String mensaje = "Se actualizó correctamente";
        response.setMensaje(mensaje);
    } else {
        String mensaje = "El usuario " + id + " no está disponible, verifíquelo en su BD";
        response.setMensaje(mensaje);
    }
    
    return response;
}

@Override
public  Respuesta<String> borrarPersona(Long id) {
    Optional<Tbpersonas> personas = 
            tbpersonaRepository.findById(id); 
     Respuesta<String> response = new Respuesta<>();
     if (personas.isPresent()) {
         tbpersonaRepository.deleteById(id);
         response.setMensaje("Se eliminó correctamente");
     }else {
         response.setMensaje("El usuario " + id + " no existe");
     }
    return response;
}
@Override
public Respuesta<String> actualizarPersona(Long id) {
	// TODO Auto-generated method stub
	return null;
}



}
