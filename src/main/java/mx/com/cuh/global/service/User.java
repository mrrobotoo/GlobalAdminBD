package mx.com.cuh.global.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.com.cuh.global.entity.TbPersonas;

public interface User {
	
	public List<TbPersonas> listaDeTodasLasPersonas();
	
	public TbPersonas guardarPersonas(TbPersonas personas);
	
	public TbPersonas obtenerPersonas(Long id);
	
	public TbPersonas actualizarPersonas(TbPersonas personas);
	
	public void eliminarPersonas (Long id);
	/*----------PAGINACION----------*/

	Page<TbPersonas> obtenerPaginaDeLasPersonas (Pageable pageable);
	
}  