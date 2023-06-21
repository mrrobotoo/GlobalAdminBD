package mx.com.cuh.global.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import mx.com.cuh.global.entity.TbPersonas;

@org.springframework.stereotype.Repository

public interface TPbersonasRepository 
extends CrudRepository<TbPersonas, Long> {

	List<TbPersonas> findAll();

	//DELETE FROM PERSON WHERE ID_PERSON =?
    void deleteById(Long idPerson);

}
