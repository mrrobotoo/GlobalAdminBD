package mx.com.cuh.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.com.cuh.global.repository.TbPersonasRepository;

@Service

public class UserImpl implements User{
	@Autowired
	private TbPersonasRepository tbPersonasRepository;
	

}
