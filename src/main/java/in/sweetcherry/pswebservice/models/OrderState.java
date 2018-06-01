package in.sweetcherry.pswebservice.models;

public class OrderState {
	private String state;
    private long employeeId;
    private String deliveryDate;
    private long deliverySlot;
    private String attemptedDetails;
    private String deliveredDetails;
    private String handoverDetails;
	
    public OrderState() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderState(String state, long employeeId, String deliveryDate, long deliverySlot, String attemptedDetails, String deliveredDetails, String handoverDetails) {
		super();
		this.state = state;
		this.employeeId = employeeId;
		this.deliveryDate = deliveryDate;
		this.deliverySlot = deliverySlot;
		this.attemptedDetails = attemptedDetails;
		this.deliveredDetails = deliveredDetails;
		this.handoverDetails = handoverDetails;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public long getDeliverySlot() {
		return deliverySlot;
	}

	public void setDeliverySlot(long deliverySlot) {
		this.deliverySlot = deliverySlot;
	}

	public String getAttemptedDetails() {
		return attemptedDetails;
	}

	public void setAttemptedDetails(String attemptedDetails) {
		this.attemptedDetails = attemptedDetails;
	}

	public String getDeliveredDetails() {
		return deliveredDetails;
	}

	public void setDeliveredDetails(String deliveredDetails) {
		this.deliveredDetails = deliveredDetails;
	}

	public String getHandoverDetails() {
		return handoverDetails;
	}

	public void setHandoverDetails(String handoverDetails) {
		this.handoverDetails = handoverDetails;
	}
    
    
}
