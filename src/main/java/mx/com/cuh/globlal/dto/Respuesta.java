package mx.com.cuh.globlal.dto;

import java.util.List;

public class Respuesta<T>{
	private String mensaje;
	private List<T> listaPersona;
	
	public List<T> geListasPersona(){
		return listaPersona;
	}
	
	public void setListasPersona(List<T>listaPersona) {
		this.listaPersona= listaPersona;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	public void setMensajes(String mensaje) {
		
	}
}