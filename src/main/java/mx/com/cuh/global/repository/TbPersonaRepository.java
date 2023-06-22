package mx.com.cuh.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import mx.com.cuh.global.entity.TbPerson;

@Service
public interface TbPersonaRepository 
 	extends CrudRepository<TbPerson, Long>{
		List<TbPerson>findAll();
		//void deletebyId(long id);
		Optional<TbPerson>findById(Long id);
	}

