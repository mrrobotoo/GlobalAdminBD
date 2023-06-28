package mx.com.cuh.global.dto;

public class PersonaDTO {
<<<<<<< HEAD
	        private String Nombre;
	        private String Edad;
	        private String Sexo;
	        
	     public String getNombre() {
				return Nombre;
			}
		public String getEdad() {
			return Edad;
		}
		public void setEdad(String edad) {
			Edad = edad;
		}
		public String getSexo() {
			return Sexo;
		}
		public void setSexo(String sexo) {
			Sexo = sexo;
		}
}
=======
	private Long id;
	private String nombre;
	private Integer edad;
	private String sexo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
}
>>>>>>> origin/JesusAvila
