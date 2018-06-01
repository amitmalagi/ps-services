package in.sweetcherry.pswebservice.errorhandler;

public class EmployeeSigninFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeSigninFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeSigninFailedException(String message) {
		super(String.format("Employee signin failed. %s", message));
		// TODO Auto-generated constructor stub
	}

}
