package mx.com.cuh.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Respuesta <T>{
	private String mensaje;
	private List<T> listaPersonas;
	
	public List<T> getListaPersonas(){
		return listaPersonas;
	}
	

	public void setListaPersonas(List<T> listasPersona) {
		this.listaPersonas = listasPersona;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
	