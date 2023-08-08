package mx.com.cuh.global.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;

@org.springframework.stereotype.Controller

public class Controller {

	@Autowired
	private Usuario usuario;

	@RequestMapping("/index") // INICIO / INDEX
	public String index() {
		return "index";
	}

	// SE MODIFICÓ PARA LA IMPLEMENTACIÓN DEL PAGINADOR
	@GetMapping("/inicio")
	public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
		int registrosCount = 100; // VARIABLE QUE ALMACENA LA CANTIDAD DE REGISTROS POR PÁGINA
		Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount));
		List<TbPerson> listaPersonas = paginaPersonas.getContent(); // OBTIENE LISTA DE USUARIOS REQUERIDOS
		model.addAttribute("listaPersonas", listaPersonas); // SE AGREGA LISTA DE USUARIOS AL MODELO DE VISTA PARA QUE EL USUARIO PUEDA VERLO
		model.addAttribute("currentPage", page); // MUESTRA LA PÁGINA ACTUAL EN LA QUE SE ENCUENTRA EL USUARIO
		model.addAttribute("totalPages", paginaPersonas.getTotalPages()); // PERMITE QUE LA VISTA MUESTRE EL TOTAL DE PÁGINAS DISPONIBLES EN EL PÁGINADOR
		return "inicio"; // RETORNA LA LISTAS DE PERSONAS
	}

	@GetMapping("/buscar")
	public String buscar(Model model, @RequestParam(defaultValue = "") String nombre, @RequestParam(defaultValue = "0") int page) {
	    int registrosCount = 100;
	    Page<TbPerson> paginaPersonas;

	    if (!nombre.trim().isEmpty()) { //VERIFICAMOS SI EL NOMBRE NO ESTÁ VACÍO DESPUÉS DE ELIMINAR ESPACIOS EN BLANCO AL INICIO Y AL FINAL.
	        paginaPersonas = usuario.obtenerPersonasPorNombre(nombre, PageRequest.of(page, registrosCount));
	    } else {
	        paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount));
	    }

	    List<TbPerson> listaPersonas = paginaPersonas.getContent();
	    model.addAttribute("listaPersonas", listaPersonas);

	    int totalPages = paginaPersonas.getTotalPages();
	    int currentPage = Math.min(page, totalPages > 0 ? totalPages - 1 : 0);
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("totalPages", totalPages);

	    boolean esBusqueda = !nombre.trim().isEmpty(); //VERIFICAMOS SI EL NOMBRE NO ESTÁ VACÍO DESPUÉS DE ELIMINAR ESPACIOS EN BLANCO AL INICIO Y AL FINAL.
	    model.addAttribute("esBusqueda", esBusqueda);

	    if (listaPersonas.isEmpty() && esBusqueda) {
	        model.addAttribute("mensaje", "No se encontraron registros para la búsqueda: " + nombre);
	    } else if (nombre.isEmpty()) {
	        return "inicio";
	    }
	    return "inicio";
	}


	@PostMapping(value = "/saveperson") // INSERTAR PERSONA
	public String insertarPersonas(@ModelAttribute PersonaDTO persona) {
		usuario.insertarPersona(persona);
		return "user";
	}

	@GetMapping("/eliminar/{id}") // ELIMINAR PERSONA
	public String eliminar(Model model, @PathVariable Long id) {
		usuario.borrarPersona(id);
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersonas", listaPersonas);
		return "redirect:/inicio";
	}

	@PostMapping(value = "/actualizar/{id}") // ACTUALIZAR PERSONA
	public String actualizarPersona(@PathVariable("id") Long id, @ModelAttribute PersonaDTO persona) {
		usuario.actualizarPersona(id, persona);
		return "redirect:/inicio";
	}


	@GetMapping("/exportar") //MÉTODO EXPORTAR PARA EXPORTAR EL PDF COMPLETO
	public ResponseEntity<byte[]> exportarTablaToZip(Model model, @RequestParam(defaultValue = "0") int page) {
	    try {
	        //CREAR EL DOCUEMTNO PDF
	        Document document = new Document();
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        PdfWriter.getInstance(document, byteArrayOutputStream);

	        //ABRIR EL DOCUMENTO ANTES DE AGREGAR CONTENIDO
	        document.open();

	        //AGREGAMOS CONTENIDO AL PDF
	        PdfPTable table = new PdfPTable(3); //CAMBIAMOS EL NÚMERO DE COLUMNAS A 3 (NOMBRE, EDAD Y SEXO) ELIMINANDO EL ID
	        table.setWidthPercentage(100);
	        table.addCell("Nombre");
	        table.addCell("Edad");
	        table.addCell("Sexo");

	        int registrosCount = 100;
	        //AGREGAMOS LA PÁGINA 0 (PRIMERA PÁGINA) CON 100 REGISTROS POR PÁGINA.
	        PageRequest pageRequest = PageRequest.of(page, registrosCount);
	        Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(pageRequest);
	        List<TbPerson> listaPersonas = paginaPersonas.getContent();

	        for (TbPerson persona : listaPersonas) {
	            //EXCLUIMOS LA COLUMNA ID Y SOLO AGREGAMOS NOMBRE, EDAD Y SEXO A LA TABLA.
	            table.addCell(persona.getNombre());
	            table.addCell(persona.getEdad().toString());
	            table.addCell(persona.getSexo());
	        }

	        document.add(table);

	        //CERRAMOS EL DOCUMENTO DESPUÉS DE AGREGAR EL CONTENIDO
	        document.close();

	        //OBTENEMOS LO BYTES DEL PDF GENERADO
	        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

	        //GENERAMOS UN NOMBRE PARA EL ACHIVO ZIP ALMACENADO TEMPORALMENTE
	        String tempZipFileName = "registros_pagina_" + (page + 1) + "_temp.zip";
	        String tempZipFilePath = "C:\\temp/" + tempZipFileName;

	        //GUARDAMOS EL PDF EN UN ARCHIVO TEMPORAL
	        String tempPdfFileName = "registros_pagina_" + (page + 1) + ".pdf";
	        String tempPdfFilePath = "C:\\temp/" + tempPdfFileName;
	        try (FileOutputStream fileOutputStream = new FileOutputStream(tempPdfFilePath)) {
	            fileOutputStream.write(pdfBytes);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        //COMPRIMIMOS EL PDF TEMPORAL EN UN ARCHIVO ZIP
	        try (FileOutputStream fileOutputStream = new FileOutputStream(tempZipFilePath);
	                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
	                FileInputStream pdfFileInputStream = new FileInputStream(tempPdfFilePath)) {

	            ZipEntry zipEntry = new ZipEntry("registros_pagina_" + (page + 1) + ".pdf");
	            zipOutputStream.putNextEntry(zipEntry);

	            byte[] buffer = new byte[1024];
	            int len;
	            while ((len = pdfFileInputStream.read(buffer)) > 0) {
	                zipOutputStream.write(buffer, 0, len);
	            }

	            zipOutputStream.closeEntry();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        //ELIMINAMOS EL ARCHIVO TEMPORAL DEL PDF
	        File tempPdfFile = new File(tempPdfFilePath);
	        tempPdfFile.delete();

	        //LEEMOS LOS BYTES DEL ARCHIVO ZIP TEMPORAL
	        Path tempZipFile = Paths.get(tempZipFilePath);
	        byte[] zipBytes = Files.readAllBytes(tempZipFile);

	        //REPORTNAMOS LOS BYTES DEL ARCHIVO ZIP EN LA RESPUESTA HTTP CON EL NOMBRE ADECUADO PARA LA DESCARGA
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "registros_pagina_" + (page + 1) + ".zip"); //ESTABLECEMOS EL NOMBRE DEL ARCHIVO ZIP PARA LA DESCARGA
	        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);

	    } catch (DocumentException | IOException e) {
	        //EN CASO DE ERROR AL CREAR EL DOCUMENTO PDF O GUARDAR EL ARCHIVO TEMPORAL
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@GetMapping("/descargas") //MAPEO DEL MÉTODO "DESCARGAS" (LISTA DE ARCHIVOS)
	public ResponseEntity<List<String>> obtenerArchivosTemporales() { 
		File carpetaTemp = new File("C:\\temp/"); //RUTA DE LA CARPETA DE ARCHIVOS TEMPORALES
		if (carpetaTemp.exists() && carpetaTemp.isDirectory()) { //VERIFICAMOS SI LA CARPETA EXISTE Y ES UN DIRECTORIO VALIDO
			File[] archivosTemporales = carpetaTemp.listFiles(); //OBTENEMOS UN ARREGLO DE ARCHIVOS Y SUBDIRECTORIOS DENTRO DE LA CARPETA TEMPORAL
			if (archivosTemporales != null) { //VERIFICAMOS SI SE ENCUENTRAN ARCHIVOS EN LA CARPETA
				List<String> nombresArchivos = Arrays.stream(archivosTemporales).map(File::getName).collect(Collectors.toList()); //CONVERTIMOS NUESTRO ARREGLO EN UNA SECUENCIA
				return ResponseEntity.ok(nombresArchivos); //RETORNAMOS LA LISTA DE NOMBRES DE ARCHIVOS
			}
		}
		return ResponseEntity.ok(Collections.emptyList()); //EN CASO DE QUE NO SE ENCUENTREN ARCHIVOS, RETORNAMOS UNA LISTA VACÍA
	}

	@GetMapping("/descargas/{nombreArchivo:.+}")
	public ResponseEntity<Resource> descargarArchivoTemporal(@PathVariable String nombreArchivo) {
	    String rutaArchivo = "C:\\temp/" + nombreArchivo; //RUTA ARCHIVOS TEMPORALES DENTRO DE NUESTRA CARPETA

	    try {
	        File archivo = new File(rutaArchivo);
	        if (archivo.exists()) {
	            String zipFileName = nombreArchivo.replace("_temp", ""); //REMOVEMOS EL "_TEMP" DEL NOMBRE DEL ARCHIVO ZIP PARA LA DESCARGA

	            //LEEMOS LOS BYTES DEL ARCHIVO
	            byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

	            //CREAMOS UN BYTEARRAYRESOURCE CON LOS BYTES DEL ARCHIVO
	            ByteArrayResource recurso = new ByteArrayResource(archivoBytes);

	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName); //ESTABLECEMOS EL NOMBRE DEL ARCHIVO ZIP PARA LA DESCARGA

	            //ENVIAMOS LA RESPUESTA HTTP CON EL ARCHIVO ZIP PARA QUE EL USUARIO LO DESCARGUE
	            ResponseEntity<Resource> response = new ResponseEntity<>(recurso, headers, HttpStatus.OK);

	            //ELIMINAMOS EL ARCHIVO ZIP DESPUÉS DE ENVIAR LA RESPUESTA
	            if (archivo.delete()) {
	                System.out.println("Archivo eliminado correctamente: " + rutaArchivo);
	            } else {
	                System.out.println("Error al eliminar el archivo: " + rutaArchivo);
	            }

	            return response;
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@DeleteMapping("/eliminar-archivo")
	public ResponseEntity<?> eliminarArchivoTemporal(@RequestParam String nombre) {
		String rutaArchivo = "C:\\temp/" + nombre; //RUTA COMPLETA DEL ARCHIVO TEMPORAL
		File archivo = new File(rutaArchivo);
		if (archivo.exists()) {
			if (archivo.delete()) {
				return ResponseEntity.ok().build(); //SI EL ARCHIVO SE ELIMINA CORRECTAMENTE
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //CUANDO OCURRA UN ERROR AL ELIMINAR EL ARCHIVO
			}
		} else {
			return ResponseEntity.notFound().build(); //EN CASO DE QUE EL ARCHIVO NO EXISTA
		}
	}



}
