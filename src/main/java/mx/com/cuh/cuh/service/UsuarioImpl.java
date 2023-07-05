package mx.com.cuh.cuh.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import mx.com.cuh.cuh.dto.Respuesta;
import mx.com.cuh.cuh.entity.TbPerson;
import mx.com.cuh.cuh.repository.TbPersonRepository;

@Service

public class UsuarioImpl implements Usuario{
	@Autowired
	private TbPersonRepository tbPersonRepository;


@Override
public Respuesta <TbPerson> obtenerpersona(){
    Respuesta<TbPerson> response = new Respuesta <TbPerson>();
    response.setListasPersona(tbPersonRepository.findAll());
    response.setMensaje("Correcto");

    return response;
    
}

}
