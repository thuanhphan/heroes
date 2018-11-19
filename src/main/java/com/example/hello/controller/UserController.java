package com.example.hello.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hello.bll.UserService;
import com.example.hello.model.User;
import com.example.hello.req.UserSignInReq;
import com.example.hello.req.UserSignUpReq;
import com.example.hello.rsp.MultipleRsp;
import com.example.hello.rsp.SingleRsp;

@RestController
@RequestMapping("/user")
public class UserController {
	// region -- Fields --

	@Autowired
	private UserService userService;

	// end
	
	// region -- Methods --
	
	/**
	 * Request new token to log in (just and only when enable login authentication,
	 * user name & password have existed in db)
	 * 
	 * @param req include (user name, password, client key, token)
	 * @return
	 */
	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(@RequestBody UserSignInReq req) {
		MultipleRsp res = new MultipleRsp();

		try {
			// Get data
			String userName = req.getUserName();
			String password = req.getPassword();

			// Handle
			User m = userService.getByUP(userName, password);
			if (m == null) {
				res.setError("User name doesn't exist!");
			} else {
				userName = m.getUserName();

				// Set data
				Map<String, Object> data = new LinkedHashMap<>();
				data.put("abc", userName);
				data.put("abc3", userName);
				res.setResult(data);
			}
		} catch (Exception ex) {
			res.setError(ex.getMessage());
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@RequestBody UserSignUpReq req) {
		SingleRsp res = new SingleRsp();

		try {
			// Get data
			String userName = req.getUserName();
			String passWord = req.getPassword();
			String firstName = req.getFirstName();
			String lastName = req.getLastName();
			String email = req.getEmail();
			
			// Convert data
			User m = new User();
			m.setEmail(email);
			m.setPassword(passWord);
			m.setFirstName(firstName);
			m.setLastName(lastName);
			m.setUserName(userName);

			// Handle
			String tmp = userService.save(m);

			if (tmp.isEmpty()) {
				// Set Data
				res.setResult("thu");
			} else {
				res.setError("User name or email have already registed!");
			}
		} catch (Exception ex) {
			res.setError(ex.getMessage());
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}


	// end
	
	
}
	

