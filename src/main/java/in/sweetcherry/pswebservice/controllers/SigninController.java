package in.sweetcherry.pswebservice.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.sweetcherry.pswebservice.errorhandler.EmployeeNotFoundException;
import in.sweetcherry.pswebservice.errorhandler.EmployeeSigninFailedException;
import in.sweetcherry.pswebservice.models.Employee;
import in.sweetcherry.pswebservice.services.EmployeeService;
import in.sweetcherry.pswebservice.services.JwtService;

@RestController
@RequestMapping("/signin")
public class SigninController {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	private final EmployeeService employeeService;
	
	private final JwtService jwtService;

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	
	@Autowired
	SigninController(EmployeeService employeeService, JwtService jwtService) {
		this.employeeService = employeeService;
		this.jwtService = jwtService;
	}
	

	/**
	 * /signin --> Authenticate an employee with the passed email and password
	 *
	 * @param email
	 *            The email to search in the database
	 * @param passwd
	 *            The password to authenticate employee
	 * @return The employee object or null if employee is not found or
	 *         authentication fails
	 * @throws ServletException
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public SigninResponse authenticateEmployee(@RequestBody final EmployeeLogin login,
			HttpServletResponse response)
			throws EmployeeSigninFailedException, EmployeeNotFoundException {
		Employee employee;

		employee = employeeService.getByEmail(login.email);

		if (login.passwd == null || login.passwd.isEmpty()) {
			throw new IllegalArgumentException("Password should not be null or empty string.");
		}

		String ps_cookie_key = employeeService.getWsProperties().getWs_cookie_key();
		String hashedPasswd = employeeService.md5Hash(ps_cookie_key + login.passwd);

		if (!hashedPasswd.equals(employee.getPasswd())) {
			throw new EmployeeSigninFailedException("Password is incorrect.");
		}
		
		//Generate and insert JWT into response header
		String token;
		try {
			token = jwtService.generateToken(employee);
			response.setHeader("Token", token);
		} catch (Exception ex) {
			throw new EmployeeSigninFailedException(ex.getMessage());
		}
		
		return new SigninResponse(token);
	}
	
	private static class EmployeeLogin {
        public String email;
        public String passwd;
    }
	
	private static class SigninResponse {
		public String token;

		public SigninResponse(String token) {
			super();
			this.token = token;
		}
		
		
	}
}
