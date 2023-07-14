
package mx.com.cuh.global.dto;

public class PersonaDTO {
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