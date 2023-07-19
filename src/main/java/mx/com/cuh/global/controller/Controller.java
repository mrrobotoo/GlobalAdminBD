package mx.com.cuh.global.controller;

import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
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
    if (page >= totalPages) {
        return "redirect:/inicio?page=" + (totalPages - 1);
    }
    model.addAttribute("listaPersonas", personasPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", personasPage.getTotalPages());
    return "inicio";
}

//@GetMapping("/exportar-pdf-zip")
//public String exportarPdfZip(Model model, @RequestParam(defaultValue = "0") int page) {
   
//}




 }

