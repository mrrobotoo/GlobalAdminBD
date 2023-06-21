package mx.com.cuh.global.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mx.com.cuh.global.dto.PersonaDTO;

public interface PersonRepository extends JpaRepository<PersonRepository, Long> {
    @Query(value = "SELECT MAX(id) FROM personas", nativeQuery = true)
    Long findMaxId();

	Long findMaxId1();

	void save(PersonaDTO person);
}
