package mx.com.cuh.global.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    
    Document document = null;
    FileOutputStream zipFile = null;
    ZipOutputStream zipOut = null;
    
    try {
        String destino = "C:\\Users\\wlemm\\Desktop\\pdfs_prueba"; // Ruta específica de destino
        String zipFileName = destino + "\\registros.zip"; // Corregir la concatenación de la ruta
        zipFile = new FileOutputStream(zipFileName);
        zipOut = new ZipOutputStream(zipFile);

        // Crear un documento PDF para cada lote de registros
        int batchSize = 100;
        int numBatches = (int) Math.ceil((double) registros.size() / batchSize);
        for (int batch = 0; batch < numBatches; batch++) {
            String pdfFileName = destino + "\\_registros_" + (batch + 1) + ".pdf";
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFileName));
            document.open();

            // Crear una tabla para los registros en el lote actual
            PdfPTable table = new PdfPTable(3); // 3 columnas (Name, Age, Sex)
            
            //table.setWidthPercentage(numBatches)
            
            Font fontCabecera = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.WHITE);
            Font fontCelda = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.WHITE);
            
            // Agregar cabecera de la tabla
            table.addCell(getSyledCell("Edad", BaseColor.DARK_GRAY, fontCabecera));
            table.addCell(getSyledCell("Nombre", BaseColor.DARK_GRAY, fontCabecera));
            table.addCell(getSyledCell("Sexo", BaseColor.DARK_GRAY, fontCabecera));

            int startIndex = batch * batchSize;
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
    } 
    	finally {
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


	
	
private PdfPCell getSyledCell(String content, BaseColor backgroundColor, Font font) {
	PdfPCell cell = new PdfPCell();
	cell.setPadding(5);
	cell.setBorderWidth(0.5f);
	cell.setBackgroundColor(backgroundColor);
	cell.setPhrase(new Phrase(content, font));
	return cell;
}
}

