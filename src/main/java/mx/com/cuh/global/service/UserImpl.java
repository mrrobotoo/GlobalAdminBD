package mx.com.cuh.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;



@Service

public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPersonasRepository;
	
	@Override
	public Respuesta <TbPersonas> obtenerRegistros(){
		Respuesta<TbPersonas> response = new Respuesta <>();
		response.setListaPersonas(tbPersonasRepository.findAll());
		response.setMensaje("Se muestra la información");
	
		return response;
	}

	@Override
	public Respuesta<String> insertarPersona(PersonasDTO persona) {
		Long idUserMax =tbPersonasRepository.obtenerMaximoId();
		
		TbPersonas NuevoRegistro = new TbPersonas();
		NuevoRegistro.setIdUser(idUserMax);
		NuevoRegistro.setName(persona.getName());
		NuevoRegistro.setAge(persona.getAge());
		NuevoRegistro.setSex(persona.getSex());
		tbPersonasRepository.save(NuevoRegistro);
		Respuesta<String> response = new Respuesta<>();
		response.setMensaje("Se insertó correctamente");
		return response;
	}
	

	
}
