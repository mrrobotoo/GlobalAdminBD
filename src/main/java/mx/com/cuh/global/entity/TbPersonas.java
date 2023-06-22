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
	
	@Column (name = "NOMBRE")
	private String name;
	
	@Column (name = "EDAD")
	private String age;
	
	@Column (name = "SEXO")
	private String sex;

	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	
}

