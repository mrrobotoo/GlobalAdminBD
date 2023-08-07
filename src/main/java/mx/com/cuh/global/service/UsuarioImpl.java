package mx.com.cuh.global.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.repository.TbPersonRepository;
import org.springframework.data.domain.Page; //SE IMPORTAN LIBRERIAS
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort; // SE IMPORTAN LIBRERIAS PARA EL PAGEREQUEST



@Service
public class UsuarioImpl implements Usuario {
	@Autowired
	private TbPersonRepository tbPersonRepository;

	@Override // SE INCLUYÓ MÉTODO PARA OBTENER A LAS PERSONAS POR PÁGINA
	public Page<TbPerson> obtenerPersonasPorPagina(PageRequest pageRequest) { //UTILIZAMOS LA CLASE "PageRequest" PARA OBTENER A LAS PERSONAS
																				//"Page<TbPerson> ES LA PÁGINA QUE VA A RETORNAR LOS RESULTADOS DE LA TABLA
		return tbPersonRepository.findAll(pageRequest); //LLAMAMOS AL METODO "findAll" DE NUESTRO REPOSITORIO CON EL NUEVO REPOSITORIO. EL NUEVO REPOSITORIO HACE POSIBLE EL USO DE NUESTRO MÉTODO "findAll".
	}

	// CREATE

	@Override
	public Respuesta<String> insertarPersona(PersonaDTO persona) {
		Long idPersonMaximo = tbPersonRepository.obtenerMaximoIdPerson();

		TbPerson personaFinal = new TbPerson();
		personaFinal.setId(idPersonMaximo);
		personaFinal.setNombre(persona.getNombre());
		personaFinal.setEdad(persona.getEdad());
		personaFinal.setSexo(persona.getSexo());

		tbPersonRepository.save(personaFinal);
		Respuesta<String> response = new Respuesta<>();
		response.setMensaje("Usuario creado correctamente");
		return response;
	}

	// READ

	@Override
	public Respuesta<TbPerson> obtenerPersonas() {
		Respuesta<TbPerson> response = new Respuesta<TbPerson>();
		response.setListasPersona(tbPersonRepository.findAll());
		response.setMensaje("ok");
		return response;
	}

	// UPDATE

	@Override
	public Respuesta<String> actualizarPersona(Long id, PersonaDTO persona) {
		TbPerson personaFinal = tbPersonRepository.findById(id).orElse(null);

		if (personaFinal != null) {
			personaFinal.setNombre(persona.getNombre());
			personaFinal.setEdad(persona.getEdad());
			personaFinal.setSexo(persona.getSexo());

			tbPersonRepository.save(personaFinal);
			Respuesta<String> response = new Respuesta<>();
			response.setMensaje("Usuario con id " + id + " actualizado");
			return response;
		} else {
			Respuesta<String> response = new Respuesta<>();
			response.setMensaje("El usuario con id " + id + " no existe");
			return response;
		}
	}

	// DELETE

	@Override
	public Respuesta<String> borrarPersona(Long id) {
		Optional<TbPerson> persona = tbPersonRepository.findById(id);
		Respuesta<String> response = new Respuesta<>();

		if (persona.isPresent()) {
			tbPersonRepository.deleteById(id);
			response.setMensaje("Usuario eliminado correctamente");
		} else {
			response.setMensaje("El usuario " + id + " no existe, favor de validar");
		}
		return response;
	}
	
	//FILTRO 
	
	@Override 
	public Page<TbPerson> obtenerPersonasPorNombre(String nombre, PageRequest pageRequest) {
	    return tbPersonRepository.findByNombreContainingIgnoreCase(nombre, pageRequest);
	}
}
