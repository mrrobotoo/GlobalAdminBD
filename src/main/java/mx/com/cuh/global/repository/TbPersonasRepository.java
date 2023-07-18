package mx.com.cuh.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import mx.com.cuh.global.entity.TbPersonas;


@org.springframework.stereotype.Repository

public interface TbPersonasRepository 
extends JpaRepository<TbPersonas, Long> {

	List<TbPersonas> findAll();
    void deleteById(Long idPerson);
    Optional<TbPersonas> findById(Long id); 
    
    @Query(value = "select nvl(max(id)+1, 1) from personas",nativeQuery = true )
    Long obtenerMaximoId();
    
	
    }


