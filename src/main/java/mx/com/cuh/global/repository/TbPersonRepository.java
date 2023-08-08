package mx.com.cuh.global.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository; //NUEVO REPOSITORIO (RECUPERA ENTIDADES MEDIANTE LA PÁGINACIÓN Y ABSTRACCIÓN DE CLASIFICACIÓN)

import mx.com.cuh.global.entity.TbPerson;

public interface TbPersonRepository extends PagingAndSortingRepository<TbPerson, Long> { //SIMPLEMENTE SE CAMBIÓ EL REPOSITORIO
	@Override
	List<TbPerson> findAll();
    @Override
	void deleteById(Long id);
    @Override
	Optional<TbPerson> findById(Long id);

    @Query(value = "select nvl(MAX(id)+1, 1) from personas", nativeQuery = true )
    Long obtenerMaximoIdPerson();

    Page<TbPerson> findByNombreContainingIgnoreCase(String nombre, Pageable pageable); //FILTRO

}
