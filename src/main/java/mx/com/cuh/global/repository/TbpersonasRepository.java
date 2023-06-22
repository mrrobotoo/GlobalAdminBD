package mx.com.cuh.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.entity.Tbpersonas;
@Service
public interface TbpersonasRepository 
extends CrudRepository<Tbpersonas, Long>{
	List<Tbpersonas>findAll();
	//void deletebyId(long id);
	Optional<Tbpersonas>findById(Long id);
	
}


