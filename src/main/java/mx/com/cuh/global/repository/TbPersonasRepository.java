package mx.com.cuh.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import mx.com.cuh.global.entity.TbPersonas;



public interface TbPersonasRepository 
extends CrudRepository<TbPersonas, Long> {

	List<TbPersonas> findAll();
    void deleteById(Long idUser);
    Optional<TbPersonas> findById(Long id); 
    
    //@Query(value = "select max(ID) +1 from personas",nativeQuery = true )
    //Long obtenerMaximoId();
    
    @Query(value = "SELECT COALESCE(MAX(ID) + 1, 1) FROM personas", nativeQuery = true)
    Long obtenerMaximoId();


}
