package com.example.hello.dal;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.hello.model.User;

public interface UserDao extends CrudRepository<User, Integer>{
	
	@Query("FROM User a WHERE a.id = :id")
	public User getBy(@Param("id") int id);
	
	//kiem tra username dc ton tai chua
	@Query("FROM User a WHERE a.userName = :userName")
	public User getBy(@Param("userName") String userName);

	@Query("FROM User a WHERE (a.userName = :userName OR a.email = :email)")
	public User getBy(@Param("userName") String userName, @Param("email") String email);
	
	@Query("FROM User a WHERE (a.userName = :userName AND a.password = :password)")
	public User getByUP(@Param("userName") String userName, @Param("password") String password);
	
}
