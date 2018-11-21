package com.example.hello.dal;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.hello.model.Users;

public interface UserDao extends CrudRepository<Users, Integer>{
	
	@Query("FROM Users a WHERE a.id = :id")
	public Users getBy(@Param("id") int id);
	
	//kiem tra username dc ton tai chua
	@Query("FROM Users a WHERE a.userName = :userName")
	public Users getBy(@Param("userName") String userName);

	@Query("FROM Users a WHERE (a.userName = :userName OR a.email = :email)")
	public Users getBy(@Param("userName") String userName, @Param("email") String email);
	
	
}
