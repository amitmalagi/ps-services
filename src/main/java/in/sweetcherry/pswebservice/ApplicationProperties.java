package in.sweetcherry.pswebservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sweetcherry")
public class ApplicationProperties {

	private String[] deliverySlots;

	public String[] getDeliverySlots() {
		return deliverySlots;
	}

	public void setDeliverySlots(String[] deliverySlots) {
		this.deliverySlots = deliverySlots;
	}
	
	
}
