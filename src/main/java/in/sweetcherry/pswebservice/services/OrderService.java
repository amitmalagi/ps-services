package in.sweetcherry.pswebservice.services;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import in.sweetcherry.pswebservice.ApplicationProperties;
import in.sweetcherry.pswebservice.PSWebServiceProperties;
import in.sweetcherry.pswebservice.errorhandler.OrderNotFoundException;
import in.sweetcherry.pswebservice.errorhandler.OrderStateNotSupportedException;
import in.sweetcherry.pswebservice.errorhandler.OrderUpdateFailedException;
import in.sweetcherry.pswebservice.models.Address;
import in.sweetcherry.pswebservice.models.AddressRepository;
import in.sweetcherry.pswebservice.models.DeliverySlot;
import in.sweetcherry.pswebservice.models.Employee;
import in.sweetcherry.pswebservice.models.EmployeeProfile;
import in.sweetcherry.pswebservice.models.Order;
import in.sweetcherry.pswebservice.models.OrderDetail;
import in.sweetcherry.pswebservice.models.OrderDetailRepository;
import in.sweetcherry.pswebservice.models.OrderHistory;
import in.sweetcherry.pswebservice.models.OrderHistoryRepository;
import in.sweetcherry.pswebservice.models.OrderRepository;
import in.sweetcherry.pswebservice.models.OrderSearch;
import in.sweetcherry.pswebservice.models.OrderSpecification;
import in.sweetcherry.pswebservice.models.OrderState;
import in.sweetcherry.pswebservice.models.OrderStateId;
import in.sweetcherry.pswebservice.models.OrderStateRepository;

@Component
public class OrderService {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	private PSWebServiceProperties wsProperties;
	
	private ApplicationProperties applicationProperties;

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private OrderStateRepository orderStateRepository;
	
	@Autowired
	private OrderHistoryRepository orderHistoryRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	public static String shipped_order_state = "shipped";
	public static String preparation_order_state = "preparation";
	public static String allocated_order_state = "allocated";
	public static String delivered_order_state = "delivered";
	public static String attempted_order_state = "attempted";
	public static String awaitingDelivery_order_state = "awaitingDelivery";
	
	private final String dateTimeFormatPattern = "yyyy/MM/dd HH:mm:ss";
	//public static String deliveryBoy_profile = "DeliveryBoy";

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	/**
	 * getAssignedOrders -> Return the list of assigned orders
	 * 
	 * 
	 * @return The list of assigned orders or empty list if no such order is
	 *         found
	 */

	public List<Order> getAllocatedOrders() {

		List<Order> orderList = orderRepository.findByOrderStateId(wsProperties.getId_allocated_order_state());
		
		for (Order order : orderList) {
			List<OrderDetail> productList = orderDetailRepository.findByOrderId(order.getOrderId());
			order.setProductList(productList);
			
			Address customerDeliveryAddress = addressRepository.findOne(order.getDeliveryAddressId());
			
			if(customerDeliveryAddress != null) {
				order.setCustomerDeliveryAddress(customerDeliveryAddress);
			}
		}

		return orderList;
	}
	
	/**
	 * findByState --> Find all orders by state
	 * 
	 * @param OrderState
	 * 			String representing the order state
	 * 
	 * @param EmployeeProfile
	 * 
	 * @return If employee profile is 'DeliveryBoy', return list of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return all orders for an input order state
	 * 
	 */
	public List<Order> findByState(String state, EmployeeProfile employeeProfile) throws OrderStateNotSupportedException {

		OrderStateId orderState = new OrderStateId(state, wsProperties);
		
		int orderStateId = orderState.getOrderStateId();
		
		if(orderStateId == 0) {
			throw new OrderStateNotSupportedException(orderStateId);
		}
		
		List<Order> orderList = null;
		
		if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
			if(orderState.isNotPart())
				orderList = orderRepository.findByOrderStateIdNotOrderByDeliveryDateAsc(orderStateId);
			else	
				orderList = orderRepository.findByOrderStateId(orderStateId);
			
		} else if (employeeProfile.getProfileId() == wsProperties.getId_deliveryboy_profile()) {
			orderList = orderRepository.findByOrderStateIdAndEmployeeId(orderStateId, employeeProfile.getEmployeeId());
		} else {
			orderList = new ArrayList<Order>();
		}
		
		for (Order order : orderList) {
			
			OrderStateId psOrderStateId = orderStateRepository.findOne(order.getOrderStateId());
			order.setOrderStateStr(psOrderStateId.getOrderState());
			
			List<OrderDetail> productList = orderDetailRepository.findByOrderId(order.getOrderId());
			order.setProductList(productList);
			
			Address customerDeliveryAddress = addressRepository.findOne(order.getDeliveryAddressId());
			
			if(customerDeliveryAddress != null) {
				order.setCustomerDeliveryAddress(customerDeliveryAddress);
			}
			
			if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
				if(order.getEmployeeId() > 0) {
				
					Employee employee = employeeService.findById(order.getEmployeeId());
				
					if (employee != null) {
						order.setEmployeeFirstName(employee.getFirstname());
						order.setEmployeeLastName(employee.getLastname());
					}
				}
			} else {
				order.setEmployeeFirstName(employeeProfile.getFirstname());
				order.setEmployeeLastName(employeeProfile.getLastname());
			}
			
			DeliverySlot deliverySlot = getDeliverySlotById((int)order.getDeliverySlotId());
			if(deliverySlot != null) {
				order.setDeliverySlot(deliverySlot);
			}
				
		}

		return orderList;
	}
	
	/**
	 * findByStatePage --> Find all orders by state, page and size
	 * 
	 * @param OrderState
	 * 			String representing the order state
	 * 
	 * @param EmployeeProfile
	 * 
	 * @return If employee profile is 'DeliveryBoy', return list of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return all orders for an input order state
	 * 
	 */
	public Page<Order> findByStatePaginated(String state, Date fromDate, Date toDate, EmployeeProfile employeeProfile, Pageable pageable) throws OrderStateNotSupportedException {

		Page<Order> orderList = null;
		OrderStateId orderState = new OrderStateId(state, wsProperties);
		
		int orderStateId = orderState.getOrderStateId();
		
		if((!state.equalsIgnoreCase(awaitingDelivery_order_state)) && (orderStateId == 0)) {
			throw new OrderStateNotSupportedException(orderStateId);
		}
		
		if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
			if(state.equalsIgnoreCase(awaitingDelivery_order_state)) {
				List<Long> orderStateIdList = orderState.getAwaitingDeliveryOrderStatesList();
				orderList = orderRepository.findByOrderStateIdInAndDeliveryDateBetweenOrderByDeliveryDateAsc(orderStateIdList, fromDate, toDate, pageable);
			} else if(orderState.isNotPart()) {
				if(fromDate != null && toDate != null)
					orderList = orderRepository.findByOrderStateIdNotAndDeliveryDateBetweenOrderByDeliveryDateAsc(orderStateId, fromDate, toDate, pageable);
				else
					orderList = orderRepository.findByOrderStateIdNotOrderByDeliveryDateAsc(orderStateId, pageable);
			} else {
				if (fromDate != null && toDate != null)
					orderList = orderRepository.findByOrderStateIdAndDeliveryDateBetweenOrderByDeliveryDateAsc(orderStateId, fromDate, toDate, pageable);
				else
					orderList = orderRepository.findByOrderStateIdOrderByDeliveryDateAsc(orderStateId, pageable);
			}
			
		} else if (employeeProfile.getProfileId() == wsProperties.getId_deliveryboy_profile()) {
			if(fromDate != null && toDate != null)
				orderList = orderRepository.findByOrderStateIdAndEmployeeIdAndDeliveryDateBetweenOrderByDeliveryDateAsc(orderStateId, employeeProfile.getEmployeeId(), fromDate, toDate, pageable);
			else
				orderList = orderRepository.findByOrderStateIdAndEmployeeIdOrderByDeliveryDateAsc(orderStateId, employeeProfile.getEmployeeId(), pageable);
		} else {
			orderList = new PageImpl(new ArrayList<Order>());
		}
		
		for (Order order : orderList) {
			
			OrderStateId psOrderStateId = orderStateRepository.findOne(order.getOrderStateId());
			order.setOrderStateStr(psOrderStateId.getOrderState());
			
			List<OrderDetail> productList = orderDetailRepository.findByOrderId(order.getOrderId());
			order.setProductList(productList);
			
			Address customerDeliveryAddress = addressRepository.findOne(order.getDeliveryAddressId());
			
			if(customerDeliveryAddress != null) {
				order.setCustomerDeliveryAddress(customerDeliveryAddress);
			}
			
			if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
				if(order.getEmployeeId() > 0) {
				
					Employee employee = employeeService.findById(order.getEmployeeId());
				
					if (employee != null) {
						order.setEmployeeFirstName(employee.getFirstname());
						order.setEmployeeLastName(employee.getLastname());
					}
				}
			} else {
				order.setEmployeeFirstName(employeeProfile.getFirstname());
				order.setEmployeeLastName(employeeProfile.getLastname());
			}
			
			DeliverySlot deliverySlot = getDeliverySlotById((int)order.getDeliverySlotId());
			if(deliverySlot != null) {
				order.setDeliverySlot(deliverySlot);
			}
				
		}

		return orderList;
	}
	
	public Page<Order> findPaginated(OrderSearch orderSearch, EmployeeProfile employeeProfile, Pageable pageable) throws OrderStateNotSupportedException {
		
		OrderSpecification spec = new OrderSpecification(orderSearch, wsProperties);
		Page<Order> orderList = orderRepository.findAll(spec, pageable);
		
		for (Order order : orderList) {
			
			OrderStateId psOrderStateId = orderStateRepository.findOne(order.getOrderStateId());
			order.setOrderStateStr(psOrderStateId.getOrderState());
			
			List<OrderDetail> productList = orderDetailRepository.findByOrderId(order.getOrderId());
			order.setProductList(productList);
			
			Address customerDeliveryAddress = addressRepository.findOne(order.getDeliveryAddressId());
			
			if(customerDeliveryAddress != null) {
				order.setCustomerDeliveryAddress(customerDeliveryAddress);
			}
			
			if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
				if(order.getEmployeeId() > 0) {
				
					Employee employee = employeeService.findById(order.getEmployeeId());
				
					if (employee != null) {
						order.setEmployeeFirstName(employee.getFirstname());
						order.setEmployeeLastName(employee.getLastname());
					}
				}
			} else {
				order.setEmployeeFirstName(employeeProfile.getFirstname());
				order.setEmployeeLastName(employeeProfile.getLastname());
			}
			
			DeliverySlot deliverySlot = getDeliverySlotById((int)order.getDeliverySlotId());
			if(deliverySlot != null) {
				order.setDeliverySlot(deliverySlot);
			}
				
		}

		return orderList;
	}
	
public List<Order> find(OrderSearch orderSearch, EmployeeProfile employeeProfile) throws OrderStateNotSupportedException {
		
		OrderSpecification spec = new OrderSpecification(orderSearch, wsProperties);
		List<Order> orderList = orderRepository.findAll(spec);
		
		for (Order order : orderList) {
			
			OrderStateId psOrderStateId = orderStateRepository.findOne(order.getOrderStateId());
			order.setOrderStateStr(psOrderStateId.getOrderState());
			
			List<OrderDetail> productList = orderDetailRepository.findByOrderId(order.getOrderId());
			order.setProductList(productList);
			
			Address customerDeliveryAddress = addressRepository.findOne(order.getDeliveryAddressId());
			
			if(customerDeliveryAddress != null) {
				order.setCustomerDeliveryAddress(customerDeliveryAddress);
			}
			
			if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
				if(order.getEmployeeId() > 0) {
				
					Employee employee = employeeService.findById(order.getEmployeeId());
				
					if (employee != null) {
						order.setEmployeeFirstName(employee.getFirstname());
						order.setEmployeeLastName(employee.getLastname());
					}
				}
			} else {
				order.setEmployeeFirstName(employeeProfile.getFirstname());
				order.setEmployeeLastName(employeeProfile.getLastname());
			}
			
			DeliverySlot deliverySlot = getDeliverySlotById((int)order.getDeliverySlotId());
			if(deliverySlot != null) {
				order.setDeliverySlot(deliverySlot);
			}
				
		}

		return orderList;
	}
	
	/**
	 * getOrderByOrderId -> Find an order given its order id
	 * 
	 * 
	 * @return The  order object or null if it is not found
	 *         
	 */

	public Order getOrderByOrderId(long orderId) {
		return orderRepository.findOne(orderId);
	}
	
	/**
	 * updateOrderState -> Update an order with a new order state
	 * 
	 * 
	 * @return The  updated order object or null if it is not found
	 *         
	 */
	
	@Transactional
	public Order updateOrderState(long orderId, final OrderState orderState) throws OrderNotFoundException, OrderUpdateFailedException {
		Order order = orderRepository.findOne(orderId);
		
		if (order != null) {
			
			final DateTimeFormatter formatter =
			         DateTimeFormatter.ofPattern(dateTimeFormatPattern);
			ZoneId zoneId = ZoneId.of("Asia/Kolkata");
			ZonedDateTime zdt = ZonedDateTime.now(zoneId).truncatedTo(ChronoUnit.SECONDS);
			
			if(orderState.getState().equalsIgnoreCase(shipped_order_state)) {
				order.setOrderStateId(wsProperties.getId_shipped_order_state());
				
				OrderHistory orderHistory = new OrderHistory(
						order.getEmployeeId(), orderId, 
						order.getOrderStateId(), formatter.format(zdt));
						
				orderRepository.save(order);
				orderHistoryRepository.save(orderHistory);
			} else if(orderState.getState().equalsIgnoreCase(allocated_order_state)) {
				order.setOrderStateId(wsProperties.getId_allocated_order_state());
				order.setEmployeeId(orderState.getEmployeeId());
				order.setDeliverySlotId(orderState.getDeliverySlot());
				//order.setDeliveryDate(Date.from(Instant.parse(orderState.getDeliveryDate())));
				
				OrderHistory orderHistory = new OrderHistory(
						orderState.getEmployeeId(), orderId, 
						order.getOrderStateId(), formatter.format(zdt));
				
				orderRepository.save(order);
				orderHistoryRepository.save(orderHistory);
			} else if(orderState.getState().equalsIgnoreCase(delivered_order_state)) {
				order.setOrderStateId(wsProperties.getId_delivered_order_state());
				if(orderState.getDeliveredDetails() != null)
					order.setDeliveredDetails(orderState.getDeliveredDetails());
				if(orderState.getHandoverDetails() != null)
					order.setHandoverDetails(orderState.getHandoverDetails());
				
				OrderHistory orderHistory = new OrderHistory(
						order.getEmployeeId(), orderId, 
						order.getOrderStateId(), formatter.format(zdt));
				
				orderRepository.save(order);
				orderHistoryRepository.save(orderHistory);
			} else if(orderState.getState().equalsIgnoreCase(attempted_order_state)) {
				order.setOrderStateId(wsProperties.getId_attempted_order_state());
				order.setAttemptedDetails(orderState.getAttemptedDetails());
				
				OrderHistory orderHistory = new OrderHistory(
						order.getEmployeeId(), orderId, 
						order.getOrderStateId(), formatter.format(zdt));
				
				orderRepository.save(order);
				orderHistoryRepository.save(orderHistory);
			} else {
				throw new OrderUpdateFailedException("Order state not supported");
			}
		} else {
			throw new OrderNotFoundException(orderId);
		}
		return order;
	}
	
	/**
	 * getDeliverySlots -> Get list of delivery time slots
	 * @return The list of configured delivery time slots from application.properties (deliverySlots)
	 */
	public List<DeliverySlot> getDeliverySlots() {
		
		List<DeliverySlot> deliverySlotList = new ArrayList<DeliverySlot>();
		
		for (int iter = 0; iter < applicationProperties.getDeliverySlots().length; ++iter) {
			deliverySlotList.add(new DeliverySlot(iter, applicationProperties.getDeliverySlots()[iter]));
		}
		return deliverySlotList;
	}
	
	/*public long countByState(String state, EmployeeProfile employeeProfile) throws OrderStateNotSupportedException {

		long count = 0;
		OrderStateId orderState = new OrderStateId(state, wsProperties);
		
		int orderStateId = orderState.getOrderStateId();
		
		if((!state.equalsIgnoreCase(awaitingDelivery_order_state)) && (orderStateId == 0)) {
			throw new OrderStateNotSupportedException(orderStateId);
		}
		
		if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
			if(state.equalsIgnoreCase(awaitingDelivery_order_state)) {
				List<Long> orderStateIdList = orderState.getAwaitingDeliveryOrderStatesList();
				count = orderRepository.countByOrderStateIdIn(orderStateIdList);
			} else {
			    count = orderRepository.countByOrderStateId(orderStateId);
			}
		} else if (employeeProfile.getProfileId() == wsProperties.getId_deliveryboy_profile()) {
			count = orderRepository.countByOrderStateIdAndEmployeeId(orderStateId, employeeProfile.getEmployeeId());
		} 
	
		return count;
	}*/
	
	public long countByState(OrderSearch orderSearch, EmployeeProfile employeeProfile) throws OrderStateNotSupportedException {

		long count = 0;
		
        OrderSpecification spec = new OrderSpecification(orderSearch, wsProperties);
		count = orderRepository.count(spec);
		
		/*OrderStateId orderState = new OrderStateId(state, wsProperties);
		
		int orderStateId = orderState.getOrderStateId();
		
		if((!state.equalsIgnoreCase(awaitingDelivery_order_state)) && (orderStateId == 0)) {
			throw new OrderStateNotSupportedException(orderStateId);
		}
		
		if(getAdminProfilesList().contains(employeeProfile.getProfileId())) {
			if(state.equalsIgnoreCase(awaitingDelivery_order_state)) {
				List<Long> orderStateIdList = orderState.getAwaitingDeliveryOrderStatesList();
				count = orderRepository.countByOrderStateIdInAndDeliveryDateBetween(orderStateIdList, fromDate, toDate);
			} else {
				count = orderRepository.countByOrderStateIdAndDeliveryDateBetween(orderStateId, fromDate, toDate);
			}
		} else if (employeeProfile.getProfileId() == wsProperties.getId_deliveryboy_profile()) {
				count = orderRepository.countByOrderStateIdAndEmployeeIdAndDeliveryDateBetween(
						orderStateId, employeeProfile.getEmployeeId(), fromDate, toDate);
		}*/
	
		return count;
	}
	
	public DeliverySlot getDeliverySlotById(int id) {
		DeliverySlot deliverySlot = null;
		if((id >= 0) && (id < applicationProperties.getDeliverySlots().length)) {
			deliverySlot = new DeliverySlot(id, applicationProperties.getDeliverySlots()[id]);
		}
		return deliverySlot;
	}
	
	public PSWebServiceProperties getWsProperties() {
		return wsProperties;
	}

	@Autowired
	public void setWsProperties(PSWebServiceProperties wsProperties) {
		this.wsProperties = wsProperties;
	}
	
	@Autowired
	public void setApplicationProperties(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}
	
	public List<Long> getAdminProfilesList() {
		return Arrays.stream(wsProperties.getId_admin_profiles())
                .boxed().collect(Collectors.<Long>toList());
	}
	
	/*private static class OrderStateId {
		
		public String orderState;
		public boolean notPart = false;
		private PSWebServiceProperties wsProperties;
		
		public OrderStateId(String orderState, PSWebServiceProperties wsProperties) {
			this.orderState = orderState;
			this.wsProperties = wsProperties;
		}
		
		public int getOrderStateId() {
			
			if(orderState.startsWith("!")) {
				notPart = true;
				orderState = orderState.substring(1);
			}
			if(orderState.equalsIgnoreCase(preparation_order_state)) {
				return wsProperties.getId_preparation_order_state();
			} else if (orderState.equalsIgnoreCase(allocated_order_state)) {
				return wsProperties.getId_allocated_order_state();
			} else if (orderState.equalsIgnoreCase(shipped_order_state)) {
				return wsProperties.getId_shipped_order_state();
			} else if (orderState.equalsIgnoreCase(attempted_order_state)) {
				return wsProperties.getId_attempted_order_state();
			} else if (orderState.equalsIgnoreCase(delivered_order_state)) {
				return wsProperties.getId_delivered_order_state();
			}  else return 0;
		}
	}*/

	
}
