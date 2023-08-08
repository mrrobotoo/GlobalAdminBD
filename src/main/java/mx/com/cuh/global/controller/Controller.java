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

	@GetMapping("/buscar") // FILTRO
	public String buscar(Model model, @RequestParam(defaultValue = "") String nombre, @RequestParam(defaultValue = "0") int page) { //USAMOS 3 PARAMETROS, MODEL: AGREGAR ATRIBUTOS Y QUE EL USUARIO LOS PUEDA VER (EN ESTE CASO, LOS REGISTROS), NOMBRE: LO USAMOS COMO PARAMETRO DE CONSULTA PARA FILTRAR A LAS PERSONAS, PAGE: NÚMERO DE PÁGINA QUE SE MOSTRARÁ EN LA PÁGINACIÓN.
		int registrosCount = 100; //CANTIDAD DE REGISTROS QUE SE MOSTRARÁN EN LA PAGINACIÓN
		Page<TbPerson> paginaPersonas; //ALMACENAMOS LOS RESULTADOS DE LAS PERSONAS

		if (nombre.isEmpty()) { //CONDICIÓNAL QUE DICE QUE SI 'nombre' (QUE ES LA VARIBLE QUE FILTRA EL NOMBRE DEL USUARIO) ENTONCES:
			paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount)); //EN CASO DE ESTAR VACÍO, OBTENDRÁ LAS PAGINAS COMPLETAS
		} else { //EN CASO CONTRARIO:
			paginaPersonas = usuario.obtenerPersonasPorNombre(nombre, PageRequest.of(page, registrosCount)); //SE OBTIENEN LAS PERSONAS FILTRADAS POR NOMBRE Y PÁGINADAS
		}

		List<TbPerson> listaPersonas = paginaPersonas.getContent(); //OBTENEMOS LA LISTA DE PERSONAS DE LA PÁGINA OBTENIDA
		model.addAttribute("listaPersonas", listaPersonas); //AGREGAMOS LA LISTA DE PERSONAS AL MODELO DECLARADO AL PRINCIPIO PARA QUE EL USUARIO PUEDA VERLA.

		//VERIFICAMOS SI EL NÚMERO SOLICITADO ESTÁ DENTRO DE LOS LÍMITES
		int totalPages = paginaPersonas.getTotalPages(); //OBTENEMOS EL NÚMERO TOTAL DE PÁGINAS DISPONIBLES DENTRO DE NUESTRO PÁGINADOR
		int currentPage = Math.min(page, totalPages > 0 ? totalPages - 1 : 0); //DETERMINAMOS LA PÁGINA ACTUAL Y NOS ASEGURAMOS QUE ESTÉ DENTRO DE LOS LÍMITES DISPONIBLES, EN CASO DE QUE LA PÁGINA SOLICITADA SEA MAYOR AL NÚMERO TOTAL DE PÁGINAS, SE RESTA UNA PARA AJUSTARLA AL NÚMERO TOTAL DE PÁGINAS.
		model.addAttribute("currentPage", currentPage); //AGREGAMOS EL NÚMERO DE PÁGINA ACTUAL AL MODELO PARA QUE EL USUARIO LO PUEDA VER.
		model.addAttribute("totalPages", totalPages); //AGREFAMOS EL NÚMERO TOTAL DE PÁGINAS PARA QUE EL USUARIO LO PUEDA VER.

		boolean esBusqueda = !nombre.isEmpty(); //VERIFICAMOS SI LA BUSQUEDA SE REALIZA EVALUANDO QUE EL PARÁMETRO "nombre" NO ESTÉ VACIO.
		model.addAttribute("esBusqueda", esBusqueda); //AGREGAMOS EL ATRIBUTO "esBusqueda" PARA INDICAR SI LA BUSQUEDA FUE REALIZADA O NO.

		//VERIFICAMOS SI LA LISTA DE PERSONAS ESTÁ VACÍA PARA MOSTRAR EL MENSAJE DE ALERTA
		if (listaPersonas.isEmpty() && esBusqueda) { //VALIDAMOS SI LA LISTA DE PERSONAS ESTÁ VACÍA Y SI LA BUSQUEDA FUE REALIZADA O NO.
			model.addAttribute("mensaje", "No se encontraron registros para la búsqueda: " + nombre); //EN CASO DE QUE NO ENCUENTRE EL NOMBRE FILTRADO
		} else if (nombre.isEmpty()) { //EN CASO DE QUE EL PARAMETRO "nombre" SE ENCUENTRE VACÍO
			//model.addAttribute("mensaje", "No se ingresó un nombre para la búsqueda."); //MENSAJE CUANDO NO SE REALIZA UNA BUSQUEDA VACÍA.
			return "inicio";
		}
		return "inicio"; //RETORNAMOS LA VISTA "inicio" DONDE SE MUESTRAN LOS RESULTADOS DE LA BUSQUEDA.
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
	        // Crear el documento PDF
	        Document document = new Document();
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        PdfWriter.getInstance(document, byteArrayOutputStream);

	        // Abrir el documento antes de agregar contenido
	        document.open();

	        // Agregar contenido al PDF
	        PdfPTable table = new PdfPTable(3); // Cambiamos el número de columnas a 3 (nombre, edad y sexo)
	        table.setWidthPercentage(100);
	        table.addCell("Nombre");
	        table.addCell("Edad");
	        table.addCell("Sexo");

	        int registrosCount = 100;
	        // Obtener la página 0 (primera página) con 100 registros por página
	        PageRequest pageRequest = PageRequest.of(page, registrosCount);
	        Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(pageRequest);
	        List<TbPerson> listaPersonas = paginaPersonas.getContent();

	        for (TbPerson persona : listaPersonas) {
	            // Excluimos la columna ID y solo agregamos nombre, edad y sexo a la tabla
	            table.addCell(persona.getNombre());
	            table.addCell(persona.getEdad().toString());
	            table.addCell(persona.getSexo());
	        }

	        document.add(table);

	        // Cerrar el documento después de agregar todo el contenido
	        document.close();

	        // Obtener los bytes del PDF generado
	        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

	        // Generar un nombre para el archivo ZIP almacenado temporalmente
	        String tempZipFileName = "registros_pagina_" + (page + 1) + "_temp.zip";
	        String tempZipFilePath = "C:\\temp/" + tempZipFileName;

	        // Guardar el PDF en un archivo temporal
	        String tempPdfFileName = "registros_pagina_" + (page + 1) + ".pdf";
	        String tempPdfFilePath = "C:\\temp/" + tempPdfFileName;
	        try (FileOutputStream fileOutputStream = new FileOutputStream(tempPdfFilePath)) {
	            fileOutputStream.write(pdfBytes);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Comprimir el PDF temporal en un archivo ZIP
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

	        // Eliminar el archivo temporal del PDF
	        File tempPdfFile = new File(tempPdfFilePath);
	        tempPdfFile.delete();

	        // Leer los bytes del archivo ZIP temporal
	        Path tempZipFile = Paths.get(tempZipFilePath);
	        byte[] zipBytes = Files.readAllBytes(tempZipFile);

	        // Retornar los bytes del archivo ZIP en la respuesta HTTP con el nombre adecuado para la descarga
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "registros_pagina_" + (page + 1) + ".zip"); // Establecer el nombre del archivo ZIP para la descarga
	        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);

	    } catch (DocumentException | IOException e) {
	        // En caso de error al crear el documento PDF o al guardar el archivo temporal
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@GetMapping("/descargas")
	public ResponseEntity<List<String>> obtenerArchivosTemporales() {
		File carpetaTemp = new File("C:\\temp/"); // Ruta de la carpeta temporal en tu sistema
		if (carpetaTemp.exists() && carpetaTemp.isDirectory()) {
			File[] archivosTemporales = carpetaTemp.listFiles();
			if (archivosTemporales != null) {
				List<String> nombresArchivos = Arrays.stream(archivosTemporales).map(File::getName).collect(Collectors.toList());
				return ResponseEntity.ok(nombresArchivos);
			}
		}
		return ResponseEntity.ok(Collections.emptyList());
	}


	@GetMapping("/descargas/{nombreArchivo:.+}")
	public ResponseEntity<Resource> descargarArchivoTemporal(@PathVariable String nombreArchivo) {
	    String rutaArchivo = "C:\\temp/" + nombreArchivo;

	    try {
	        File archivo = new File(rutaArchivo);
	        if (archivo.exists()) {
	            String zipFileName = nombreArchivo.replace("_temp", ""); // Removemos el "_temp" del nombre del archivo ZIP para la descarga

	            // Leer los bytes del archivo
	            byte[] archivoBytes = Files.readAllBytes(archivo.toPath());

	            // Crear un ByteArrayResource con los bytes del archivo
	            ByteArrayResource recurso = new ByteArrayResource(archivoBytes);

	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName); // Establece el nombre del archivo ZIP para la descarga

	            // Enviar la respuesta HTTP con el archivo ZIP para que el usuario lo descargue
	            ResponseEntity<Resource> response = new ResponseEntity<>(recurso, headers, HttpStatus.OK);

	            // Eliminar el archivo ZIP después de enviar la respuesta
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
		String rutaArchivo = "C:\\temp/" + nombre; // Ruta completa del archivo temporal en tu sistema
		File archivo = new File(rutaArchivo);
		if (archivo.exists()) {
			if (archivo.delete()) {
				return ResponseEntity.ok().build(); // El archivo se ha eliminado correctamente
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error al eliminar el archivo
			}
		} else {
			return ResponseEntity.notFound().build(); // El archivo no existe
		}
	}



}
