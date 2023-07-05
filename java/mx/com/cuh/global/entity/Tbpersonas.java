package mx.com.cuh.global.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name= "personas")

public class Tbpersonas {
    @Id
    @Column (name = "ID")
    private Long id;
    
   
    @Column (name = "Nombre")
    private String nombre;
    
    @Column (name = "EDAD")
    private String edad;
    
    @Column (name = "SEXO")
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