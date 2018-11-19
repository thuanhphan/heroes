package com.example.hello.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user", schema = "public")


public class User {
		
		// region -- Fields --

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq_generator")
		@SequenceGenerator(name = "user_id_seq_generator", sequenceName = "public.user_id_seq", allocationSize = 1)
		@Column(columnDefinition = "SERIAL")
		private Integer id;
		
		@Column(columnDefinition = "varchar(64)")
		private String userName;
		
		@Column(columnDefinition = "varchar(120)")
		private String email;

		@Column(columnDefinition = "varchar(64)")
		private String password;

		@Column(columnDefinition = "varchar(64)")
		private String firstName;
		
		@Column(columnDefinition = "varchar(64)")
		private String lastName;
		
		
		// end
		
		// region -- Get set --
		
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getUserName() {
			return userName;
		}
		
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		
			
		//end
		
		// region -- Methods --

		public User() {
			
		}
		//end
		
		
		
		
		
	
	
}
