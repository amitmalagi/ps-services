package in.sweetcherry.pswebservice.models;

public class DeliverySlot {
	private int id;
	private String slot;
	
	public DeliverySlot() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DeliverySlot(int id, String slot) {
		super();
		this.id = id;
		this.slot = slot;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}
	
	
}
