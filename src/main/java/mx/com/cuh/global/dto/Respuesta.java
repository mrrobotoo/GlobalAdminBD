package mx.com.cuh.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import mx.com.cuh.global.entity.TbPersonas;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Respuesta <T>{

	private String mensaje;

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void getMensaje(Respuesta<String> borrarPersona) {
		// TODO Auto-generated method stub
	}

	public List<TbPersonas> getListaPersonas() {
		// TODO Auto-generated method stub
		return null;
	}
}
	