package mx.com.cuh.global.dto;

import java.util.List;

<<<<<<< HEAD
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

=======
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Respuesta<T>{
	private String mensaje;
    private List<T> listasPersona;
    
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public List<T> getListasPersona() {
		return listasPersona;
	}
	public void setListasPersona(List<T> listasPersona) {
		this.listasPersona = listasPersona;
	} 

}
>>>>>>> origin/JesusAvila
