package mx.com.cuh.global.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "personas")
public class TbPersonas {

	@Id
	@Column (name = "ID")
	private Long idUser;
	
	@Column (name = "Nombre")
	private String name;
	
	@Column (name = "EDAD")
	private String age;
	
	@Column (name = "SEXO")
	private String sex;
}