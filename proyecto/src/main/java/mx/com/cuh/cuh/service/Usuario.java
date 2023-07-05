package mx.com.cuh.cuh.service;

import mx.com.cuh.cuh.dto.Respuesta;
import mx.com.cuh.cuh.entity.TbPerson;
public interface Usuario {

    Respuesta<TbPerson> obtenerpersona();
    
   
    
}
