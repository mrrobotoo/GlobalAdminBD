package mx.com.cuh.global.dto;

import java.util.List;


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
		
	}
}
