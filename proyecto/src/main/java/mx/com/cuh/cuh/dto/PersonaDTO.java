package mx.com.cuh.cuh.dto;

public class PersonaDTO {
	 private Long idUsuario;
	    private String Nombre;
	    private String Edad;
	    private String Sexo;
	    
	    public Long getidUsuario() {
	        return idUsuario;
	    }
	    public void setidUsuario(Long idUsuario) {
	        this.idUsuario = idUsuario;
	    }
	    
	    public String GetNombre() {
	        return Nombre;
	    }
	    public void SetNombre(String Nombre) {
	        this.Nombre = Nombre;
	    }
	    public String getEdad() {
	        return Edad;
	    }
	    public void SetEdad(String Edad) {
	        this.Edad = Edad;
	    }
	    
	    public String GetSexo() {
	        return Sexo;
	    }
	    public void SetSexo(String Sexo) {
	        this.Sexo = Sexo;
	    }
	}

   