package mx.com.cuh.global.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import mx.com.cuh.global.dto.PersonasDTO;
import mx.com.cuh.global.dto.Respuesta;
import mx.com.cuh.global.entity.TbPersonas;
import mx.com.cuh.global.repository.TbPersonasRepository;
import mx.com.cuh.global.service.User;

@org.springframework.stereotype.Controller
public class Controller {

    private static final int RECORDS_PER_PAGE = 10; //Numero de Registros por pagina

    @Autowired
    private User user;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio(Model model, @RequestParam(name = "page", defaultValue = "1") int page) {
        // Obtener la respuesta con los registros
        Respuesta respuesta = user.obtenerRegistros();
        // Obtener la lista completa de personas
        List<TbPersonas> listaPersonas = respuesta.getListaPersonas();

        // Calcular el número total de registros y páginas
        int totalRecords = listaPersonas.size();
        int totalPages = (int) Math.ceil((double) totalRecords / RECORDS_PER_PAGE);

        // Calcular los índices de inicio y fin para la página actual
        int startIndex = (page - 1) * RECORDS_PER_PAGE;
        int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, totalRecords);

        // Obtener la sublista de personas correspondiente a la página actual
        List<TbPersonas> paginatedList = listaPersonas.subList(startIndex, endIndex);

        // Agregar los atributos al modelo para enviarlos a la vista
        model.addAttribute("listaPersonas", paginatedList); // Lista de personas de la página actual
        model.addAttribute("totalPages", totalPages); // Número total de páginas
        model.addAttribute("currentPage", page); // Página actual

        return "inicio"; // Nombre de la vista a la que se redirige
    }

    @PostMapping(value = "/saveperson")
    public String insertarPersona(
            @ModelAttribute PersonasDTO persona, RedirectAttributes attributes) {
        user.insertarPersona(persona);
        return "user";
    }

    @GetMapping("/eliminar/{idUser}")
    public String eliminar(@PathVariable Long idUser) {
        user.borrar(idUser);
        return "redirect:/inicio";
    }

    @PostMapping(value = "/actualizar/{idUser}")
    public String actualizarPersona(@PathVariable Long idUser,
                                    @ModelAttribute PersonasDTO persona) {
        user.actualizarPersona(idUser, persona);
        return "redirect:/inicio";
    }

}
