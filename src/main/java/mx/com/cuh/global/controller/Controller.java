package mx.com.cuh.global.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private User user;

    @Value("${cosa.cosa")
    private String valor;

    private String archivo;

    private ByteArrayResource resource;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping(value = "/saveperson")
    public String insertarPersona(@ModelAttribute PersonasDTO persona) {
        user.insertarPersona(persona);
        return "user";
    }

    @GetMapping("/eliminar/{idUser}")
    public String eliminar(@PathVariable Long idUser) {
        user.borrar(idUser);
        return "redirect:/inicio";
    }

    @PostMapping(value = "/actualizar/{idUser}")
    public String actualizarPersona(@PathVariable Long idUser, @ModelAttribute PersonasDTO persona) {
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

    @GetMapping("/exportar-pdf-zip")
    public ResponseEntity<ByteArrayResource> exportarPdfZip() {
        return user.exportarPdfZip();
    }

    @GetMapping("/obtener-nombres-zip-descargados")
    public ResponseEntity<List<String>> obtenerNombresArchivosZipDescargados(
            @RequestParam(defaultValue = "0") int page) {
        String rutaDirectorio = "C:\\Users\\DILLAN\\Desktop\\pruebas\\pdf";
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
        String rutaDirectorio = "C:\\Users\\DILLAN\\Desktop\\pruebas\\pdf";
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
        String rutaDirectorio = "C:\\Users\\DILLAN\\Desktop\\pruebas\\pdf"; // Ruta del directorio donde están los archivos ZIP
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
        String rutaDirectorio = "C:\\Users\\DILLAN\\Desktop\\pruebas\\pdf";
        List<String> nombresArchivos = obtenerNombresArchivosZipDescargados(rutaDirectorio);

        int pageSize = 10;
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