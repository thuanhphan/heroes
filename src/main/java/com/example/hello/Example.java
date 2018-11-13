package com.example.hello;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class Example {
	
	//--Fields--
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_id_seq_generator")
	@SequenceGenerator(name = "common_id_seq_generator", sequenceName = "public.common_id_seq", allocationSize = 1)
	@Column(columnDefinition = "SERIAL")
	private Integer id;
	
	@Column(columnDefinition = "varchar(64)")
	private String first_name;

	@Column(columnDefinition = "varchar(64)")
	private String last_name;

	
	
	//end
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	//--Methods--
	public Example() {
		
	}

}
