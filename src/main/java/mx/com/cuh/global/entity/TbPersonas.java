package mx.com.cuh.global.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "personas")
public class TbPersonas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (name = "nombre",nullable = false, length = 100)
	private String nombre;
	
	@Column (name = "edad", nullable = false, length = 100)
	private String edad;
	
	@Column (name = "sexo", nullable = false, length = 10)
	private String sexo;

	public TbPersonas() {
		
	}

	public TbPersonas(Long id, String nombre, String edad, String sexo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.edad = edad;
		this.sexo = sexo;
	}
	
	public TbPersonas(String nombre, String edad, String sexo) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.sexo = sexo;
	}

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

	@Override
	public String toString() {
		return "TbPersonas [id=" + id + ", nombre=" + nombre + ", edad=" + edad + ", sexo=" + sexo + "]";
	}
	
	
	
}