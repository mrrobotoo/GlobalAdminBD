package mx.com.cuh.global.service;

import java.awt.print.Pageable;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonRepository;
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
	
	public Respuesta<String> borrar(Long idUser) {
        Optional<TbPersonas> persona =
                tbPersonasRepository.findById(idUser);
        Respuesta<String> response = new Respuesta<>();
        
        if (persona.isPresent()) {
            tbPersonasRepository.deleteById(idUser);
            response.setMensaje("Se eliminó correctamente");
        } else {
            response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
        }
        
        return response;
   }
	
    public Respuesta<String> actualizarPersona(long idUser, PersonasDTO personasDTO) {
        Optional<TbPersonas> persona = tbPersonasRepository.findById(idUser);
        Respuesta<String> response = new Respuesta<>();

        if (persona.isPresent()) {
            TbPersonas personaExistente = persona.get();
            personaExistente.setName(personasDTO.getName());
            personaExistente.setAge(personasDTO.getAge());
            personaExistente.setSex(personasDTO.getSex());

            tbPersonasRepository.save(personaExistente);

            response.setMensaje("Se actualizó correctamente");
        } else {
            response.setMensaje("El usuario con ID " + idUser + " no existe, favor de validar");
        }

        return response;
    }

	@Override
	public Respuesta<String> borrarPersona(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Respuesta<String> insertarPersona(PersonaDTO persona) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Respuesta<String> actualizarPersona(Long id, PersonasDTO personas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<TbPersonas> getAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}
}
	