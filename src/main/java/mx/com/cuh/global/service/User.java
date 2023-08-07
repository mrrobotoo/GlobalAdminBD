package mx.com.cuh.global.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import mx.com.cuh.global.entity.TbPersonas;

public interface User {

	public List<TbPersonas> listaDeTodasLasPersonas();

	public TbPersonas guardarPersonas(TbPersonas personas);

	public TbPersonas obtenerPersonas(Long id);

	public TbPersonas actualizarPersonas(TbPersonas personas);

	public void eliminarPersonas(Long id);

	/*------------------------------------------------------------------------------*/
	/*----------PAGINACION----------*/
	Page<TbPersonas> obtenerPaginaDeLasPersonas(Pageable pageable);

	/*----------VISUALIZACION DE ARCHIVOS ZIP----------*/
	List<String> obtenerNombreArchivosZIPDescargados(String rutaDirectorio);

	/*----------DESCARGA y DESCARGAR DE ARCHIVOS ZIP----------*/
	/*ResponseEntity<FileSystemResource> descargarZip(String nombreArchivo);*/
    ResponseEntity<StreamingResponseBody> descargarZip(String nombreArchivo);



	ResponseEntity<String> eliminarArchivo(String nombreArchivo);

}