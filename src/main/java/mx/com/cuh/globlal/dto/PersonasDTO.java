package mx.com.cuh.globlal.dto;

public class PersonasDTO {
	
	private String Nombre;
	private String Edad;
	private Long IdPerson;
	private String sexo;
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getEdad() {
		return Edad;
	}
	public void setEdad(String edad) {
		Edad = edad;
	}
	public Long getIdPerson() {
		return IdPerson;
	}
	public void setIdPerson(Long idPerson) {
		IdPerson = idPerson;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	

}
