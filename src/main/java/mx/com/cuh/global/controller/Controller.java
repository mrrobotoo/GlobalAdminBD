package mx.com.cuh.global.controller;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.itextpdf.text.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;
import mx.com.cuh.global.service.User;


@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;
	
@Value("${cosa.cosa")
private String valor;
	
 @RequestMapping("/")
	 public String index() {
		 return "index";
	 }

 @PostMapping(value = "/saveperson")
 public String insertarPersona(
         @ModelAttribute PersonasDTO persona) {
     user.insertarPersona(persona);
     return  "user";
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
public ResponseEntity<ByteArrayResource> exportarPdfZip() {
    return user.exportarPdfZip();
}
}