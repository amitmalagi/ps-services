package in.sweetcherry.pswebservice.errorhandler;

public class OrderUpdateFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderUpdateFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderUpdateFailedException(String message) {
		super(String.format("Order update failed. %s", message));
		// TODO Auto-generated constructor stub
	}

}
