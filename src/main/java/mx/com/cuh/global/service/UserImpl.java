 package mx.com.cuh.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;

@Service
public class UserImpl implements User {
	@Autowired
	private TbPersonasRepository repository;

	@Override
	public List<TbPersonas> listaDeTodasLasPersonas() {
		return repository.findAll();
	}

	@Override
	public TbPersonas guardarPersonas(TbPersonas personas) {
		return repository.save(personas);
	}

	@Override
	public TbPersonas obtenerPersonas(Long id) {
		return repository.findById(id).get();
	}

	@Override
	public TbPersonas actualizarPersonas(TbPersonas personas) {
		return repository.save(personas);
	}

	@Override
	public void eliminarPersonas(Long id) {
		repository.deleteById(id);
		
	}
	

	
}






