package in.sweetcherry.pswebservice.errorhandler;

public class EmployeeNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeNotFoundException(String email) {
		super(String.format("Employee with email %s does not exist", email));
		// TODO Auto-generated constructor stub
	}
	
	public EmployeeNotFoundException(long id) {
		super(String.format("Employee with id %d does not exist", id));
		// TODO Auto-generated constructor stub
	}


}
