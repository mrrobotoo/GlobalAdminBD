package mx.com.cuh.global.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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

	/*----------PAGINACION----------*/
	@Override
	public Page<TbPersonas> obtenerPaginaDeLasPersonas(Pageable pageable) {
		return repository.findAll(pageable);
	}

	/*----------VISUALIZACION DE ARCHIVOS ZIP----------*/
	@Override
	public List<String> obtenerNombreArchivosZIPDescargados(String rutaDirectorio) {
		List<String> nombresArchivos = new ArrayList<>();

		File directorio = new File(rutaDirectorio);
		if (directorio.exists() && directorio.isDirectory()) {
			File[] archivos = directorio.listFiles();
			if (archivos != null) {
				for (File archivo : archivos) {
					if (archivo.isFile() && archivo.getName().endsWith(".zip")) {
						nombresArchivos.add(archivo.getName());
					}
				}
			}
		}

		return nombresArchivos;
	}

	/*----------DESCARGA DE ARCHIVOS ZIP----------*/

	private final String rutaDirectorio = "C:\\Users\\Javier HDZ M\\Desktop\\Registros de Personas";

	/*@Override
	public ResponseEntity<FileSystemResource> descargarZip(String nombreArchivo) {
		String rutaArchivo = rutaDirectorio + "\\" + nombreArchivo;

		File archivo = new File(rutaArchivo);
		if (archivo.exists()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", nombreArchivo);
			headers.setContentLength(archivo.length());

			FileSystemResource resource = new FileSystemResource(archivo);

			return ResponseEntity.ok().headers(headers).body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}*/
	
	@Override
	public ResponseEntity<StreamingResponseBody> descargarZip(String nombreArchivo) {
		 String rutaArchivo = rutaDirectorio + "\\" + nombreArchivo;

	        File archivo = new File(rutaArchivo);
	        if (archivo.exists()) {
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	            headers.setContentDispositionFormData("attachment", nombreArchivo);
	            headers.setContentLength(archivo.length());

	            FileSystemResource resource = new FileSystemResource(archivo);

	            StreamingResponseBody responseBody = outputStream -> {
	                try {
	                    org.apache.commons.io.IOUtils.copy(resource.getInputStream(), outputStream);
	                    resource.getInputStream().close();
	                    eliminarArchivoDespuesDescarga(archivo); // Llamamos a la función para eliminar el archivo después de descargarlo
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            };

	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .body(responseBody);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    private void eliminarArchivoDespuesDescarga(File archivo) {
	        if (archivo.delete()) {
	            System.out.println("Archivo eliminado correctamente después de la descarga.");
	        } else {
	            System.err.println("No se pudo eliminar el archivo después de la descarga.");
	        }
	    }
	
	
	/*----------ELIMINAR DE ARCHIVOS ZIP----------*/
	
	@Override
	public ResponseEntity<String> eliminarArchivo(String nombreArchivo) {
		String rutaArchivo = rutaDirectorio + "\\" + nombreArchivo;

		File archivo = new File(rutaArchivo);
		if (archivo.exists()) {
			if (archivo.delete()) {
				return ResponseEntity.ok("{\"mensaje\": \"Archivo eliminado exitosamente.\"}");
			} else {
				return ResponseEntity.badRequest().body("{\"mensaje\": \"Error al eliminar el archivo.\"}");
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}



}