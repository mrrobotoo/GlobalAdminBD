package mx.com.cuh.global.dto;

import java.util.List;

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
		
	}
}