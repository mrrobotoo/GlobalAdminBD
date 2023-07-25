package mx.com.cuh.global.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

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

import mx.com.cuh.global.dto.PersonaDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPerson;
import mx.com.cuh.global.service.Usuario;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@org.springframework.stereotype.Controller

public class Controller {
	
	@Autowired
	private Usuario usuario;
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	//@Value("${cosa.cosa}")
	//private String valor;
	
	@GetMapping("/inicio")
		public String inicio(Model model, @RequestParam(defaultValue = "0") int page) { //int page: sirve para especificar la página que se va a visualizar.
																						
		    int registrosCount = 100; //Cuantos registros queremos que aparescan por página.
		    Page<TbPerson> paginaPersonas = usuario.obtenerPersonasPorPagina(PageRequest.of(page, registrosCount)); //Con registrosCount cuenta la cantidad de registros que se especifico anteriormente.
		    																										
		    List<TbPerson> listaPersonas = paginaPersonas.getContent(); 
		    model.addAttribute("listaPersonas", listaPersonas); 
		    model.addAttribute("currentPage", page); //HAce que se visualice la página actual en la que se encuentra.
		    model.addAttribute("totalPages", paginaPersonas.getTotalPages()); //Aparece cuantas páginas tiene.
		    return "inicio"; 
		}
	
	@PostMapping(value = "/saveperson")
	public String insertarPersonas(
			@ModelAttribute PersonaDTO persona) {
		usuario.insertarPersona(persona);
		return "user";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(Model model, @PathVariable Long id){
		usuario.borrarPersona(id);
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		model.addAttribute("listaPersonas", listaPersonas);
		return "redirect:/inicio";
	}
	
	@PostMapping(value = "/actualizar/{id}") //ACTUALIZAR PERSONA
	public String actualizarPersona( @PathVariable Long id, @ModelAttribute PersonaDTO persona){
		List<TbPerson> listaPersonas = usuario.obtenerPersonas().getListasPersona();
		usuario.actualizarPersona(id,persona);
			return "redirect:/inicio";
		} 
	
	
	@GetMapping("/exportar-pdf")
	public void exportarPdf(HttpServletResponse response) {
	    try {
	        byte[] pdfContent = generarPdf();
	        response.setContentType("application/zip");
	        response.setHeader("Content-Disposition", "attachment; filename = registros.zip");
	        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
	        ZipEntry pdfEntry = new ZipEntry("registros.pdf");
	        zipOut.putNextEntry(pdfEntry);
	        zipOut.write(pdfContent);
	        zipOut.closeEntry();
	        zipOut.finish();
	        zipOut.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    private byte[] generarPdf() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            Respuesta<TbPerson> respuesta = usuario.obtenerPersonas();
            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            addRows(table, respuesta.getListasPersona());
            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Paragraph("Nombre"));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPhrase(new Paragraph("Edad"));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setPhrase(new Paragraph("Sexo"));
        table.addCell(cell);
    }

    private void addRows(PdfPTable table, List<TbPerson> personas) {
        int registrosMaximos = 10; //LIMITA CANTIDAD DE ELEMENTOS AL REGISTRAR
        int registrosAgregados = 0;

        for (TbPerson persona : personas) {
            if (registrosAgregados >= registrosMaximos) {
                break;
            }

            table.addCell(persona.getNombre());
            table.addCell(String.valueOf(persona.getEdad()));
            table.addCell(persona.getSexo());

            registrosAgregados++;
        }
    }
    /* ALMACENAR ARCHIVOS PDF EN CARPETAS TEMPORALES DENTRO DEL DISCO DURO C:
    private void guardarPdfEnServidor(byte[] pdfContent) {
        try {
            Path tempDirectory = Paths.get("C:\\pdf_files");
            Path pdfPath = tempDirectory.resolve("registros.pdf");
            Files.write(pdfPath, pdfContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

	
}
			  
	
    
	
	
	


	


