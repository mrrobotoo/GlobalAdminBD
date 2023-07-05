package mx.com.cuh.cuh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name= "persona")

public class TbPerson {
    @Id
    @Column (name = "ID")
    private Long idUsuario;
    
   
    @Column (name = "Nombre")
    private String nombre;
    
    @Column (name = "EDAD")
    private String edad;
    
    @Column (name = "SEXO")
    private String sexo;
    

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
    
    }
