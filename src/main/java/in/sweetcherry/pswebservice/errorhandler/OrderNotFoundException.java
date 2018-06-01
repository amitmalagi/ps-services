package in.sweetcherry.pswebservice.errorhandler;

public class OrderNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderNotFoundException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderNotFoundException(long orderId) {
		super(String.format("Order with order id %d does not exist", orderId));
		// TODO Auto-generated constructor stub
	}

}
