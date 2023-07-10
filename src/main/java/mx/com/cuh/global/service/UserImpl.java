package mx.com.cuh.global.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;


@Service
public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPbersonasRepository;
	
@Override
  	public Respuesta<String> borrarPersona(Long idUser){
	tbPbersonasRepository.findById(idUser);
	Respuesta<String> response = new Respuesta<>();
	
		return response;
	}

	@Override
	public Respuesta<String> insertarPersona(PersonasDTO persona) {
		Long idUserMax =tbPbersonasRepository.obtenerMaximoId();
		
		TbPersonas NuevoRegistro = new TbPersonas();
		NuevoRegistro.setIdUser(idUserMax);
		NuevoRegistro.setName(persona.getName());
		NuevoRegistro.setAge(persona.getAge());
		NuevoRegistro.setSex(persona.getSex());
		tbPbersonasRepository.save(NuevoRegistro);
		Respuesta<String> response = new Respuesta<>();
		response.setMensaje("Se insertó correctamente");
		return response;
	}
	
	@Override
    public Respuesta<String> borrar(Long idUser) {
        Optional<TbPersonas> persona =
        		tbPbersonasRepository.findById(idUser);
        Respuesta<String> response = new Respuesta<>();
        
        if (persona.isPresent()) {
        	tbPbersonasRepository.deleteById(idUser);
            response.setMensaje("Se eliminó correctamente");
        } else {
            response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
        }
        
        return response;
   }
	
public Respuesta<String> insertaPersona(PersonasDTO persona) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Respuesta<TbPersonas> obtenerRegistros() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Respuesta<String> actualizarPersona(long idPerson, PersonasDTO personasDTO) {
	// TODO Auto-generated method stub
	return null;
}

}

