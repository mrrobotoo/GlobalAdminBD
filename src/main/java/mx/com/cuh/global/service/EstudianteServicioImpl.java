package mx.com.cuh.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.entity.Estudiante;
import mx.com.cuh.global.repository.EstudianteRepositorio;

@Service
public class EstudianteServicioImpl implements EstudianteServicio{
	
	@Autowired
	private EstudianteRepositorio repositorio;
	@Override
	public List<Estudiante> listarTodosLosEstudiantes() {
		return repositorio.findAll();
	} 
	

}
