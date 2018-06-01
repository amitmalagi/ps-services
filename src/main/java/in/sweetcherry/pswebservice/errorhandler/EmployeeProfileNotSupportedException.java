package in.sweetcherry.pswebservice.errorhandler;

public class EmployeeProfileNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeProfileNotSupportedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeProfileNotSupportedException(String profile) {
		super(String.format("Employee profile with %s not supported", profile));
		// TODO Auto-generated constructor stub
	}

}
