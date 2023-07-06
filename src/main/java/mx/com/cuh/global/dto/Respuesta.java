package mx.com.cuh.global.dto;

import java.util.List;

<<<<<<< HEAD

public class Respuesta <T>{
	private String mensaje;
	private List<T> listaPersona;
	
	public List<T> getListasPersona() {
		return listaPersona;
	}
	
	public void setListasPersona(List<T> listaPersona) {
		this.listaPersona = listaPersona;
	}
	
	public String getMensjae() {
		return mensaje;
	}
	
	public void setMensaje(String mensaje) {
=======
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Respuesta <T>{
	private String Mensaje;
	private List<T> listaPersonas;
	
	public List<T> getListaPersonas(){
		return listaPersonas;
	}
	

	public void setListaPersonas(List<T> listasPersona) {
		this.listaPersonas = listasPersona;

	


	}


	public void setMensaje(String string) {
		// TODO Auto-generated method stub
>>>>>>> origin/Flor
		
	}
}

