package mx.com.cuh.global.dto;

import java.util.List;

public class Respuesta {

	private String mensaje;
	private List<PersonaDTO> listasPersona;

	public List<PersonaDTO> getListasPersona() {
		return listasPersona;
	}

	public void setListasPersona(List<PersonaDTO> listasPersona) {
		this.listasPersona = listasPersona;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}

