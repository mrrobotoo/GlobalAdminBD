package mx.com.cuh.global.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.zip.ZipEntry;
import java.io.File;

import java.util.zip.ZipOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller

public class Controller {

	@Autowired
	private User servicio;

	/*----------Lista de Usuarios-----------
		@GetMapping({ "/personas", "/" })
		public String listaDePersonas(Model modelo) {
			modelo.addAttribute("personas", servicio.listaDeTodasLasPersonas());
			return "personas";
		}*/
	/*----------CODIGO DE PAGINADOR-----------*/
	@GetMapping({ "/personas", "/", "inicio" })
	public String listaDePersonas(Model modelo, @RequestParam(defaultValue = "0") int page) {
		if (page < 0) {
			return "redirect:/inicio";
		}

		int pageSize = 10;
		PageRequest pageRequest = PageRequest.of(page, pageSize);
		Page<TbPersonas> personasPage = servicio.obtenerPaginaDeLasPersonas(pageRequest);

		int totalPages = personasPage.getTotalPages();
		int startPageIndex = Math.max(0, page - 1);
		int endPageIndex = Math.min(totalPages - 1, page + 1);

		modelo.addAttribute("personas", personasPage.getContent());
		modelo.addAttribute("currentPage", page);
		modelo.addAttribute("totalPages", personasPage.getTotalPages());
		modelo.addAttribute("startPageIndex", startPageIndex);
		modelo.addAttribute("endPageIndex", endPageIndex);

		return "personas";
	}

	/*----------Mostrar Usuarios-----------*/
	@GetMapping("/personas/nuevo")
	public String mostrarPersonasRegistradas(Model modelo) {
		TbPersonas personas = new TbPersonas();
		modelo.addAttribute("personas", personas);
		return "crear_personas";
	}

	/*----------Guardar Usuarios-----------*/
	@PostMapping("/personas")
	public String guardarPersonas(@ModelAttribute("personas") TbPersonas personas) {
		servicio.guardarPersonas(personas);
		return "redirect:/personas";
	}

	/*----------Editar Usuario-----------*/
	/* CODIGO ORIGINAL */
	@GetMapping("/personas/editar/{id}")
	public String mostrarPersonasAEditar(@PathVariable Long id, Model modelo) {
		modelo.addAttribute("personas", servicio.obtenerPersonas(id));
		return "/editar_personas";
	}

	/*----------Actualizar usuario-----------*/
	@PostMapping("/personas/{id}")
	public String actualizarPersona(@PathVariable Long id, @ModelAttribute("personas") TbPersonas personas,
			Model modelo) {
		TbPersonas personaExistente = servicio.obtenerPersonas(id);
		personaExistente.setId(id);
		personaExistente.setNombre(personas.getNombre());
		personaExistente.setEdad(personas.getEdad());
		personaExistente.setSexo(personas.getSexo());

		servicio.actualizarPersonas(personaExistente);
		return "redirect:/personas";
	}

	/*----------Eliminar Usuario-----------*/
	@GetMapping("/personas/{id}")
	public String eliminarPersonas(@PathVariable Long id) {
		servicio.eliminarPersonas(id);
		return "redirect:/personas";
	}

	/*----------CONVERTIR TABLA DE PERSONAS-A PDF-----------*/

	@GetMapping("/exportar-pdf-zip")
	public String exportarPdfZip(Model model) {
		List<TbPersonas> registros = servicio.listaDeTodasLasPersonas();

		Document document = null;
		FileOutputStream zipFile = null;
		ZipOutputStream zipOut = null;

		int batchCount = obtenerUltimoNumeroArchivoZip("C:\\Users\\Javier HDZ M\\Desktop\\Registros de Personas") + 1;

		try {
			String destino = "C:\\Users\\Javier HDZ M\\Desktop\\Registros de Personas"; // Ruta específica de destino
			String zipFileName = destino + "\\registros_" + batchCount + ".zip";
			zipFile = new FileOutputStream(zipFileName);
			zipOut = new ZipOutputStream(zipFile);

			// Crear un documento PDF para cada lote de registros
			int batchSize = 100;
			int numBatches = (int) Math.ceil((double) registros.size() / batchSize);
			for (int batch = 0; batch < numBatches; batch++) {
				String pdfFileName = destino + "\\registros" + (batch + 1) + ".pdf";
				document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
				document.open();

				// Crear una tabla para los registros en el lote actual
				PdfPTable table = new PdfPTable(3); // 3 columnas (Name, Age, Sex)

				// Agregar cabecera de la tabla
				table.addCell("Edad");
				table.addCell("Nombre");
				table.addCell("Sexo");

				int startIndex = batch * batchSize;
				int endIndex = Math.min(startIndex + batchSize, registros.size());
				for (int i = startIndex; i < endIndex; i++) {
					TbPersonas registro = registros.get(i);
					table.addCell(String.valueOf(registro.getEdad()));
					table.addCell(registro.getNombre());
					table.addCell(registro.getSexo());
				}

				document.add(table);
				document.close();

				// Agregar el PDF al archivo ZIP
				File pdfFile = new File(pdfFileName);
				FileInputStream pdfIn = new FileInputStream(pdfFile);
				ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
				zipOut.putNextEntry(zipEntry);
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = pdfIn.read(buffer)) > 0) {
					zipOut.write(buffer, 0, bytesRead);
				}
				pdfIn.close();

				// Borrar el archivo PDF después de agregarlo al ZIP
				pdfFile.delete();
			}

			zipOut.close();
			zipFile.close();

			return "redirect:/inicio";
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
			// Mensaje de error
			return "error";
		} finally {
			try {
				if (document != null) {
					document.close();
				}
				if (zipOut != null) {
					zipOut.close();
				}
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int obtenerUltimoNumeroArchivoZip(String destino) {
		File dir = new File(destino);
		File[] files = dir.listFiles((dir1, name) -> name.matches("registros_\\d+\\.zip"));
		int ultimoNumero = 0;
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				int numero = Integer.parseInt(fileName.substring(fileName.indexOf('_') + 1, fileName.lastIndexOf('.')));
				ultimoNumero = Math.max(ultimoNumero, numero);
			}
		}
		return ultimoNumero;
	}
	/* FIN DEL CODIGO */

	/*----------CODIGO DE VISUALIZACION - ZIP ----------*/

	@GetMapping("/obtener-nombres-zip-descargados")
	public ResponseEntity<List<String>> obtenerNombreArchivosZIPDescargados() {
		String rutaDirectorio = "C:\\Users\\Javier HDZ M\\Desktop\\Registros de Personas";
		List<String> nombresArchivos = servicio.obtenerNombreArchivosZIPDescargados(rutaDirectorio);
		if (!nombresArchivos.isEmpty()) {
			return ResponseEntity.ok(nombresArchivos);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/*----------DESCARGA DE ARCHIVOS ZIP----------*/

	@GetMapping("/descargar-zip/{nombreArchivo}")
	public ResponseEntity<FileSystemResource> descargarZip(@PathVariable String nombreArchivo) {
		return servicio.descargarZip(nombreArchivo);
	}

	/*----------ELIMINAR DE ARCHIVOS ZIP----------*/
	@PostMapping("/eliminarArchivo/{nombreArchivo}")
	public ResponseEntity<String> eliminarArchivo(@PathVariable String nombreArchivo) {
		return servicio.eliminarArchivo(nombreArchivo);
	}
}
