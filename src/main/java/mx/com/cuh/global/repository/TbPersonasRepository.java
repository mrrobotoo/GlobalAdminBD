package mx.com.cuh.global.repository;

import org.springframework.data.repository.CrudRepository;

import mx.com.cuh.global.entity.TbPersonas;


@org.springframework.stereotype.Repository

public interface TbPersonasRepository 
extends CrudRepository<TbPersonas, Long> {

}
