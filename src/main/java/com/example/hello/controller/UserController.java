package com.example.hello.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hello.bll.SettingService;
import com.example.hello.bll.UserService;
import com.example.hello.common.Const;
import com.example.hello.config.JwtTokenUtil;
import com.example.hello.dal.SettingDao;
import com.example.hello.dto.PayloadDto;
import com.example.hello.model.AuthToken;
import com.example.hello.model.Setting;
import com.example.hello.model.Users;
import com.example.hello.req.UserSignInReq;
import com.example.hello.req.UserSignUpReq;
import com.example.hello.rsp.MultipleRsp;
import com.example.hello.rsp.SingleRsp;
import com.example.hello.common.Utils;

@RestController
@RequestMapping("/user")
public class UserController {
	// region -- Fields --

	@Autowired
	private UserService userService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	// end
	
	// region -- Methods --
	
	@PostMapping("/update")
	public ResponseEntity<?> getInfo(@RequestHeader HttpHeaders header, @RequestBody UserSignUpReq req) {
		SingleRsp res = new SingleRsp();
		
		try {
			PayloadDto pl = Utils.getTokenInfor(header);
			int userId = pl.getId();
			
			String firstName = req.getFirstName();
			String lastName = req.getLastName();
						
			Users m = userService.getBy(userId);
			m.setFirstName(firstName);
			m.setLastName(lastName);
			
			String abc = userService.save(m);
			
			if(abc.isEmpty()) {
				res.setResult("");
				res.setMessage("Update success!");
			}
			else {
				res.setError("Loi cmnr");				
			}
			
		}
		catch(Exception ex) {
			res.setError(ex.toString());
		}
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@PostMapping("/info")
	public ResponseEntity<?> getInfo(@RequestHeader HttpHeaders header) {
		SingleRsp res = new SingleRsp();
		
		try {
			PayloadDto pl = Utils.getTokenInfor(header);
			int userId = pl.getId();
						
			Users m = userService.getBy(userId);
			
			res.setResult(m);
		}
		catch(Exception ex) {
			
		}
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
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
			String clientKey = req.getClientKey();
			String token = req.getToken();
			boolean sendToken = req.isSendToken();

			// Handle
			Users m = userService.getBy(userName, userName);
			if (m == null) {
				res.setError("User name doesn't exist!");
			} else {
				userName = m.getUserName();
				UsernamePasswordAuthenticationToken x;
				x = new UsernamePasswordAuthenticationToken(userName, password);
				Authentication y = authenticationManager.authenticate(x);
				SecurityContextHolder.getContext().setAuthentication(y);
				int userId = m.getId();

				// Set data
				Map<String, Object> data = new LinkedHashMap<>();
				if (sendToken) {
					Setting t1 = settingService.getBy(userId, Const.Setting.CODE_LOGIN);
					String t2 = "";
					if (t1 != null) {
						if (Const.STATUS_ACTIVE.equals(t1.getStatus())) {
							t2 = t1.getValue() + "";
						}
					}

					AuthToken m1 = null;
					switch (t2) {
					case Const.Setting.CODE_TOKEN:
						m1 = userService.generateToken(Const.Module.SIGN_IN, userId, t2);
						String t3 = m1.getClientKey();
						data.put("authen", true);
						data.put("key", t3);
						break;

//					case Const.Setting.CODE_OTP:
//						m1 = userService.generateToken(Const.Module.SIGN_IN, userId, t2);
//						t3 = m1.getClientKey();
//
//						// Send SMS
//						String t4 = m.getContactNo();
//						String t5 = m1.getToken();
//						MessageService.send(t4, t5);
//
//						data.put("authen", true);
//						data.put("key", t3);
//						break;

					default:
						List<SimpleGrantedAuthority> z = userService.getRole(m.getId());
						t3 = jwtTokenUtil.doGenerateToken(m, z);
						data.put("authen", false);
						data.put("key", t3);
						break;
					}
				} else {
					userService.verifyToken(clientKey, userId, token, m.getUuid());

					List<SimpleGrantedAuthority> z = userService.getRole(m.getId());
					String t1 = jwtTokenUtil.doGenerateToken(m, z);
					data.put("key", t1);
				}
				res.setResult(data);
			}
		} catch (AuthenticationException e) {
			res.setError("Unauthorized/Invalid user name/email or password!");
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
			String firstName = req.getFirstName();
			String lastName = req.getLastName();
			String email = req.getEmail();

			String password = req.getPassword();
			password = bCryptPasswordEncoder.encode(password);
			
			// Convert data
			Users m = new Users();
			m.setEmail(email);
			m.setPasswordHash(password);
			m.setFirstName(firstName);
			m.setLastName(lastName);
			m.setUserName(userName);

			// Handle
			String tmp = userService.save(m);

			if (tmp.isEmpty()) {
				List<SimpleGrantedAuthority> z = userService.getRole(m.getId());
				String token = jwtTokenUtil.doGenerateToken(m, z);

				// Set Data
				res.setResult(token);
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
	

