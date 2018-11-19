package com.example.hello.bll;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hello.dal.UserDao;
import com.example.hello.model.User;



@Service(value = "userService")
@Transactional
public class UserService {
	
	@Autowired
	private UserDao userDao;

	@PersistenceContext
	private EntityManager entityManager;

	// end

	// region -- Methods --

	public User getBy(int id) {
		User res = userDao.getBy(id);
		return res;
	}

	public User getBy(String userName, String email) {
		User res = userDao.getBy(userName, email);
		return res;
	}
	
	public User getByUP(String userName, String password) {
		User res = userDao.getByUP(userName, password);
		return res;
	}

	public String save(User m) {
		String res = "";

		Integer id = m.getId();
		String userName = m.getUserName();
		String email = m.getEmail();

		User m1;
		if (id == null || id == 0) {
			m1 = userDao.getBy(userName, email);
			if (m1 != null) {
				res = "Duplicate data";
			} else {
				userDao.save(m);
			}
		} else {
			m1 = userDao.getBy(id);
			if (m1 == null) {
				res = "Id does not exist";
			} else {

				m1.setFirstName(m.getFirstName());
				m1.setLastName(m.getLastName());
				m1.setEmail(m.getEmail());

				userDao.save(m1);
			}
		}

		return res;
	}

	

}
