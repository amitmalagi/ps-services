package in.sweetcherry.pswebservice.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import in.sweetcherry.pswebservice.PSWebServiceProperties;
import in.sweetcherry.pswebservice.services.OrderService;

@Entity
@Table(name="ps_order_state_lang")
public class OrderStateId {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id_order_state")
	private long orderStateId;
	
	@Column(name="name")
	private String orderState;
	
        @Transient
	private boolean notPart = false;
	
        @Transient
	private PSWebServiceProperties wsProperties;

        public OrderStateId() {
                super();
                        // TODO Auto-generated constructor stub
        }
	
	public OrderStateId(long orderStateId, String orderState) {
		super();
		this.orderStateId = orderStateId;
		this.orderState = orderState;
	}

	public OrderStateId(String orderState, PSWebServiceProperties wsProperties) {
		this.orderState = orderState;
		this.wsProperties = wsProperties;
	}
	
	public int getOrderStateId() {
		
		if(orderState.startsWith("!")) {
			notPart = true;
			orderState = orderState.substring(1);
		}
		if(orderState.equalsIgnoreCase(OrderService.preparation_order_state)) {
			return wsProperties.getId_preparation_order_state();
		} else if (orderState.equalsIgnoreCase(OrderService.allocated_order_state)) {
			return wsProperties.getId_allocated_order_state();
		} else if (orderState.equalsIgnoreCase(OrderService.shipped_order_state)) {
			return wsProperties.getId_shipped_order_state();
		} else if (orderState.equalsIgnoreCase(OrderService.attempted_order_state)) {
			return wsProperties.getId_attempted_order_state();
		} else if (orderState.equalsIgnoreCase(OrderService.delivered_order_state)) {
			return wsProperties.getId_delivered_order_state();
		}  else return 0;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public boolean isNotPart() {
		return notPart;
	}

	public void setNotPart(boolean notPart) {
		this.notPart = notPart;
	}

	public void setOrderStateId(long orderStateId) {
		this.orderStateId = orderStateId;
	}
	
	public List<Long> getAwaitingDeliveryOrderStatesList() {
		return Arrays.stream(wsProperties.getId_awaiting_delivery_order_states())
                .boxed().collect(Collectors.<Long>toList());
	}
	
	
}
