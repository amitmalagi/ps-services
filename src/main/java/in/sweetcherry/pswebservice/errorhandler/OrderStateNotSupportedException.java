package in.sweetcherry.pswebservice.errorhandler;

public class OrderStateNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderStateNotSupportedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderStateNotSupportedException(int orderState) {
		super(String.format("Order state %d is not supported", orderState));
		// TODO Auto-generated constructor stub
	}

}
