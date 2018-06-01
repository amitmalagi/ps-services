package in.sweetcherry.pswebservice.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sweetcherry.pswebservice.errorhandler.EmployeeProfileNotSupportedException;
import in.sweetcherry.pswebservice.errorhandler.OrderNotFoundException;
import in.sweetcherry.pswebservice.errorhandler.OrderStateNotSupportedException;
import in.sweetcherry.pswebservice.errorhandler.OrderUpdateFailedException;
import in.sweetcherry.pswebservice.models.Employee;
import in.sweetcherry.pswebservice.models.EmployeeProfile;
import in.sweetcherry.pswebservice.models.Order;
import in.sweetcherry.pswebservice.services.EmployeeService;
import in.sweetcherry.pswebservice.services.OrderService;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------
	
	private final EmployeeService employeeService;
	

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	
	@Autowired
	EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	/**
	 * /findByProfile --> Find all employees by profile
	 * 
	 * @param Profile
	 * 			String representing the employee profile
	 * 
	 * @return , List of employees filtered by input profile 
	 * @throws EmployeeProfileNotSupportedException 
	 */
	@RequestMapping(value = "findByProfile", method = RequestMethod.GET)
	public EmployeeList getEmployeesByProfile(@RequestParam("profile") final String profile, 
			HttpServletRequest request) throws EmployeeProfileNotSupportedException {
		
		return new EmployeeList(employeeService.findByProfile(profile));
		
	}
		
	private static class EmployeeList {
		@SuppressWarnings("unused")
		public List<Employee> items;
		
		public EmployeeList(List<Employee> items) {
			this.items = items;
		}
	}
	
}
