package mx.com.cuh.global.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personas")
public class Estudiante {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		
		private Long id ;
		
		@Column (name = "NOMBRE")
		private String name;
		
		@Column (name = "EDAD")
		private String age;
		
		@Column (name = "SEXO")
		private String sex;

		public Estudiante(String name, String age, String sex) {
			super();
			this.name = name;
			this.age = age;
			this.sex = sex;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
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

		@Override
		public String toString() {
			return "Estudiante [id=" + id + ", name=" + name + ", age=" + age + ", sex=" + sex + "]";
		}
		
		
}
