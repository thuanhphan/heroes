package com.example.hello.bll;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hello.common.Const;
import com.example.hello.common.Enums;
import com.example.hello.common.Utils;
import com.example.hello.dal.AuthTokenDao;
import com.example.hello.dal.RoleDao;
import com.example.hello.dal.UserDao;
import com.example.hello.model.AuthToken;
import com.example.hello.model.Users;

@Service(value = "userService")
@Transactional
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private AuthTokenDao authTokenDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// end

	// region -- Methods --

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Users u = userDao.getBy(userName);

		if (u == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		List<String> roles = roleDao.getRoleByUserId(u.getId());
		String hash = u.getPasswordHash();

		return new User(userName, hash, getAuthority(roles));
	}	

	public Users getBy(int id) {
		Users res = userDao.getBy(id);
		return res;
	}

	public Users getBy(String userName, String email) {
		Users res = userDao.getBy(userName, email);
		return res;
	}
	
	public List<SimpleGrantedAuthority> getRole(int id) {
		List<String> roles = roleDao.getRoleByUserId(id);
		List<SimpleGrantedAuthority> res = getAuthority(roles);
		return res;
	}
	
	private List<SimpleGrantedAuthority> getAuthority(List<String> roles) {
		return roles.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
	}

	public String save(Users m) {
		String res = "";

		Integer id = m.getId();
		String userName = m.getUserName();
		String email = m.getEmail();

		Users m1;
		if (id == null || id == 0) {
			m1 = userDao.getBy(userName, email);
			if (m1 != null) {
				res = "Duplicate data";
			} else {
				m.setUuid(UUID.randomUUID());
				m.setCreateBy(1);
				m.setCreateOn(new Date());

				userDao.save(m);
			}
		} else {
			m1 = userDao.getBy(id);
			if (m1 == null) {
				res = "Id does not exist";
			} else {
				m1.setModifyBy(1);
				m1.setModifyOn(new Date());

				m1.setFirstName(m.getFirstName());
				m1.setLastName(m.getLastName());
				m1.setEmail(m.getEmail());

				userDao.save(m1);
			}
		}

		return res;
	}

	
	public AuthToken generateToken(String module, int userId, String type) throws Exception {
		AuthToken m = authTokenDao.getBy("", module, userId);

		if (m == null) {
			m = new AuthToken();
		}

		m.setCreateBy(userId);
		m.setCreateOn(new Date());

		String clientKey = bCryptPasswordEncoder.encode(new Date().toString());
		m.setClientKey(clientKey);

		String token = "";
		if (Const.Setting.CODE_TOKEN.equals(type)) {
			token = Const.Setting.CODE_TOKEN;
		} else if (Const.Setting.CODE_OTP.equals(type)) {
			token = Utils.getToken();
		} else {
			int n = Const.Authentication.TOKEN_NUMBER;
			token = Utils.getToken(n);
		}
		m.setToken(token);

		m.setModule(module);
		Date d = Utils.getTime(Calendar.MINUTE, Const.Authentication.TOKEN_MINUTE);
		m.setExpireOn(d);

		// Reset data
		m.setVerified(false);
		m.setModifyBy(null);
		m.setModifyOn(null);

		authTokenDao.save(m);

		return m;
	}
	
	public void verifyToken(String clientKey, int userId, String token, UUID uuid) throws Exception {
		AuthToken m = authTokenDao.getBy(clientKey, "", userId);

		if (m == null) {
			throw new Exception(Enums.Error.E201.toString());
		}

		Date t = m.getExpireOn();
		if (!Utils.verify(t)) {
			throw new Exception(Enums.Error.E202.toString());
		}

		String authKey = m.getToken();
		if (Const.Setting.CODE_TOKEN.equals(authKey)) {
			Date d = new Date();
			SimpleDateFormat f = new SimpleDateFormat(Const.DateTime.TOKEN);
			f.setTimeZone(TimeZone.getTimeZone("UTC"));
			String s = f.format(d);
			s += uuid;
			int n = Const.Authentication.TOKEN_NUMBER;
			authKey = Utils.getToken(s, n);
		}

		if (!authKey.equals(token)) {
			throw new Exception(Enums.Error.E203.toString());
		}

		m.setToken(token);
		m.setVerified(true);
		m.setModifyOn(new Date());
		m.setModifyBy(userId);

		authTokenDao.save(m);
	}


}
