package mx.com.cuh.global.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller
public class Controller {
	@Autowired
	private User user;

	@GetMapping("/")
    public String redirectInicio() {
        return "redirect:/inicio";
    }
	
	@GetMapping("index")
	public String index() {
		return "index";
	}

	@GetMapping("/inicio")
    public String inicio(Model model, @RequestParam(defaultValue = "0") int page) {
        return user.inicio(model, page);
    }

	@PostMapping(value = "/saveperson")
	public String insertarPersona(@ModelAttribute PersonasDTO persona) {
		user.insertarPersona(persona);
		return "index";
	}

	@GetMapping("/eliminar/{idUser}")
	public String eliminar(@PathVariable Long idUser, @RequestParam(defaultValue = "0") int page) {
		user.borrar(idUser);
		return "redirect:/inicio?page=" + page;
	}

	@PostMapping(value = "/actualizar/{idUser}")
	public String actualizarPersona(@PathVariable Long idUser, @ModelAttribute PersonasDTO persona) {
		user.actualizarPersona(idUser, persona);
		return "redirect:/inicio";
	}

	
	@GetMapping("/exportar-pdf")
    public String exportarPdf(HttpServletRequest request) {
        // Llamar al método exportarPdf de la instancia de UserImpl
        user.exportarPdf();
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

	
	@ModelAttribute("listaArchivosZip")
    public List<String> obtenerListaArchivosZip() {
        return user.obtenerListaArchivosZip();
    }
	
	@GetMapping("/descargarArchivo/{archivo}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable String archivo) throws IOException {
        Resource resource = user.descargarArchivo(archivo);
        if (resource != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo);
            return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	
	@GetMapping("/eliminarArchivo/{archivo}")
    public String eliminarArchivo(@PathVariable String archivo, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mensaje;
        String referer = request.getHeader("referer");
        try {
            boolean eliminado = user.eliminarArchivo(archivo);
            if (eliminado) {
                mensaje = "Archivo eliminado exitosamente.";
            } else {
                mensaje = "Error al eliminar el archivo.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error al eliminar el archivo.";
        }
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        return "redirect:" + referer;
    }
	
}
