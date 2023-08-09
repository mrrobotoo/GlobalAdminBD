package mx.com.cuh.global.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.util.Map;
import java.util.HashMap;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;


@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;
	
 @RequestMapping("/")
	 public String index() {
		 return "index";
	 }

 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "redirect:/";
 	} 

 @GetMapping("/eliminar/{idUser}")
 public String eliminar(@PathVariable Long idUser) {
     user.borrar(idUser);
     return "redirect:/inicio";
 }

@PostMapping(value = "/actualizar/{idUser}")
public String actualizarPersona(@PathVariable
		Long idUser, 
	@ModelAttribute PersonasDTO persona) {
	user.actualizarPersona(idUser, persona);
    return "redirect:/inicio";
}

@GetMapping("inicio")
public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
    if (page < 0) {
           return "redirect:/inicio";
       }
    int pageSize = 10;
    PageRequest pageRequest = PageRequest.of(page, pageSize);
    Page<TbPersonas> personasPage = user.obtenerRegistroPaginados(pageRequest);
    int totalPages = personasPage.getTotalPages();
    int startPageIndex = Math.max(0, page - 1);
    int endPageIndex = Math.min(totalPages - 1, page + 1);
    if (page >= totalPages) {
        return "redirect:/inicio?page=" + (totalPages - 1);
    }
    model.addAttribute("listaPersonas", personasPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", personasPage.getTotalPages());
    model.addAttribute("startPageIndex", startPageIndex);
    model.addAttribute("endPageIndex", endPageIndex);
    return "inicio";
}

@GetMapping("/exportar-pdf-zip")
public String exportarPdfZip(Model model) {
    List<TbPersonas> registros = user.obtenerTodosLosRegistros();

    Collections.sort(registros, Comparator.comparingLong(TbPersonas::getIdUser));

    ZipOutputStream zipOut = null;

    try {
        String destino = "C:\\Users\\Flor Luna\\Documents\\Gestion\\PDF-ZIP"; // Ruta específica de destino
        int zipCounter = 1; // Contador para el número del ZIP
        String zipFileName = destino + "\\registros_" + zipCounter + ".zip"; // Nombre único del archivo ZIP

        while (new File(zipFileName).exists()) {
            // Verificar si el archivo ZIP ya existe, si es así, incrementar el contador
            zipCounter++;
            zipFileName = destino + "\\registros_" + zipCounter + ".zip";
        }

        zipOut = new ZipOutputStream(new FileOutputStream(zipFileName)); // Abrir en modo de escritura

        // Crear un documento PDF para cada lote de registros
        int batchSize = 100;
        int batchNumber = 1;
        int startIndex = 0;

        while (startIndex < registros.size()) {
            // Crear un ByteArrayOutputStream para el PDF actual
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, pdfOutputStream);
            document.open();

            // Crear una tabla para los registros en el lote actual
            PdfPTable table = new PdfPTable(3); // 3 columnas (Name, Age, Sex)

            Font fontCabecera = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.WHITE);

            // Agregar cabecera de la tabla
            table.addCell(getSyledCell("Edad", BaseColor.DARK_GRAY, fontCabecera));
            table.addCell(getSyledCell("Nombre", BaseColor.DARK_GRAY, fontCabecera));
            table.addCell(getSyledCell("Sexo", BaseColor.DARK_GRAY, fontCabecera));

            int endIndex = Math.min(startIndex + batchSize, registros.size());
            for (int i = startIndex; i < endIndex; i++) {
                TbPersonas registro = registros.get(i);
                table.addCell(String.valueOf(registro.getAge()));
                table.addCell(registro.getName());
                table.addCell(registro.getSex());
            }

            document.add(table);
            document.close();

            // Agregar el PDF al archivo ZIP
            ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
            ZipEntry zipEntry = new ZipEntry("registros" + batchNumber + ".pdf"); // Nombre único con el contador
            zipOut.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pdfInputStream.read(buffer)) > 0) {
                zipOut.write(buffer, 0, bytesRead);
            }
            pdfInputStream.close();

            startIndex = endIndex;
            batchNumber++;
        }

        zipOut.close();

        return "redirect:/inicio";
    } catch (IOException | DocumentException e) {
        e.printStackTrace();
        // Mensaje de error
        return "error";
    } finally {
        try {
            if (zipOut != null) {
                zipOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


	
	
private PdfPCell getSyledCell(String content, BaseColor backgroundColor, Font font) {
	PdfPCell cell = new PdfPCell();
	cell.setPadding(5);
	cell.setBorderWidth(0.5f);
	cell.setBackgroundColor(backgroundColor);
	cell.setPhrase(new Phrase(content, font));
	return cell;
}

@GetMapping("/obtener-nombres-zip-descargados")
public ResponseEntity<List<String>> obtenerNombresArchivosZipDescargados(
        @RequestParam(defaultValue = "0") int page) {
    String rutaDirectorio = "C:\\Users\\Flor Luna\\Documents\\Gestion\\PDF-ZIP";
    List<String> nombresArchivos = obtenerNombresArchivosZipDescargados(rutaDirectorio);

    int pageSize = 5;
    int totalPages = (int) Math.ceil((double) nombresArchivos.size() / pageSize);

    int startIndex = page * pageSize;
    int endIndex = Math.min(startIndex + pageSize, nombresArchivos.size());

    if (startIndex >= nombresArchivos.size()) {
        return ResponseEntity.notFound().build();
    }

    List<String> archivosPaginados = nombresArchivos.subList(startIndex, endIndex);

    return ResponseEntity.ok(archivosPaginados);
}

// Función para obtener los nombres de los archivos ZIP descargados en el directorio especificado
private List<String> obtenerNombresArchivosZipDescargados(String rutaDirectorio) {
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

@GetMapping("/descargar-zip/{nombreArchivo}")
public ResponseEntity<FileSystemResource> descargarZip(@PathVariable String nombreArchivo) {
    String rutaDirectorio = "C:\\Users\\Flor Luna\\Documents\\Gestion\\PDF-ZIP";
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
}

@PostMapping("/eliminarArchivo/{nombreArchivo}")
public ResponseEntity<String> eliminarArchivo(@PathVariable String nombreArchivo) {
    String rutaDirectorio = "C:\\Users\\Flor Luna\\Documents\\Gestion\\PDF-ZIP"; // Ruta del directorio donde están los archivos ZIP
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

@GetMapping("/obtener-nombres-zip-descargados-popup2")
public ResponseEntity<Map<String, Object>> obtenerNombresArchivosZipDescargadosPopup2(
        @RequestParam(defaultValue = "0") int page) {
    String rutaDirectorio = "C:\\Users\\Flor Luna\\Documents\\Gestion\\PDF-ZIP";
    List<String> nombresArchivos = obtenerNombresArchivosZipDescargados(rutaDirectorio);

    int pageSize = 5;
    int totalPages = (int) Math.ceil((double) nombresArchivos.size() / pageSize);

    int startIndex = page * pageSize;
    int endIndex = Math.min(startIndex + pageSize, nombresArchivos.size());

    if (startIndex >= nombresArchivos.size()) {
        return ResponseEntity.notFound().build();
    }

    List<String> archivosPaginados = nombresArchivos.subList(startIndex, endIndex);

    Map<String, Object> response = new HashMap<>();
    response.put("archivosPaginados", archivosPaginados);
    response.put("currentPage", page);
    response.put("totalPages", totalPages);

    return ResponseEntity.ok(response);
}

}