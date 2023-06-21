package mx.com.cuh.global.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.com.cuh.global.dto.PersonaDTO;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping
    public ResponseEntity<String> createPerson(@RequestBody PersonaDTO person) {
        try {
            // Obtener el ID máximo actual
            Long maxId = (personRepository).findMaxId1();
            Long newId = (maxId != null) ? maxId + 1 : 1;

            // Establecer el nuevo ID y guardar la persona en la base de datos
            person.setId(newId);
            personRepository.save(person);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario");
        }
    }
}
