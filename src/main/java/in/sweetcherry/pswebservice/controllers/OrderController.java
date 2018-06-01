package in.sweetcherry.pswebservice.controllers;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sweetcherry.pswebservice.errorhandler.OrderNotFoundException;
import in.sweetcherry.pswebservice.errorhandler.OrderStateNotSupportedException;
import in.sweetcherry.pswebservice.errorhandler.OrderUpdateFailedException;
import in.sweetcherry.pswebservice.models.DeliverySlot;
import in.sweetcherry.pswebservice.models.EmployeeProfile;
import in.sweetcherry.pswebservice.models.Order;
import in.sweetcherry.pswebservice.models.OrderSearch;
import in.sweetcherry.pswebservice.models.OrderSpecification;
import in.sweetcherry.pswebservice.models.OrderState;
import in.sweetcherry.pswebservice.services.OrderService;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/orders")
public class OrderController {

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------
	
	private final OrderService orderService;
	

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	
	@Autowired
	OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	/**
	 * /findByState --> Find all orders by state
	 * 
	 * @param OrderState
	 * 			String representing the order state
	 * 
	 * @return If employee profile is 'DeliveryBoy', return list of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return all orders for an input order state
	 * 
	 */
	@RequestMapping(value = "findByState", method = RequestMethod.GET)
	public OrderList getOrdersByState(@RequestParam("orderState") final String orderState, 
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		return new OrderList(orderService.findByState(orderState, employeeProfile));
		
	}
	
	/**
	 * /findByStatePaginated --> Find all orders by state by specifying page and size
	 * 
	 * @param OrderState
	 * 			String representing the order state
	 * @param FromDate
	 * 			Date representing from date filter (optional)
	 * @param ToDate
	 * 			Date representing to date filter (optional)
	 * @param Page
	 * 			Integer representing the page number
	 * @param Size
	 * 			Integer representing the page size
	 * 
	 * @return If employee profile is 'DeliveryBoy', return list of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return all orders for an input order state
	 * 
	 */
	@RequestMapping(value = "findByStatePaginated", method = RequestMethod.GET)
	public OrderList getOrdersByStatePaginated(@RequestParam("orderState") final String orderState, 
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") final Date fromDate, 
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="dd-MM-yyyy") final Date toDate,
			HttpServletRequest request, Pageable pageable) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		return new OrderList(orderService.findByStatePaginated(orderState, fromDate, toDate, employeeProfile, pageable));
		
	}
	
	@RequestMapping(value = "find", method = RequestMethod.GET)
	public OrderList getOrders(@ModelAttribute OrderSearch orderSearch,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		orderSearch.setEmployeeId(employeeProfile.getEmployeeId());
		orderSearch.setProfileId(employeeProfile.getProfileId());
		
		return new OrderList(orderService.find(orderSearch, employeeProfile));
		
	}
	
	@RequestMapping(value = "findPaginated", method = RequestMethod.GET)
	public OrderList getOrdersPaginated(@ModelAttribute OrderSearch orderSearch,
			HttpServletRequest request, Pageable pageable) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		orderSearch.setEmployeeId(employeeProfile.getEmployeeId());
		orderSearch.setProfileId(employeeProfile.getProfileId());
		
		return new OrderList(orderService.findPaginated(orderSearch, employeeProfile, pageable));
		
	}
	
	/**
	 * /countByState --> Get count of all orders by state
	 * 
	 * @param OrderState
	 * 			String representing the order state
	 * @param FromDate
	 * 			Date representing from date filter (optional)
	 * @param ToDate
	 * 			Date representing to date filter (optional)
	 * 
	 * @return If employee profile is 'DeliveryBoy', return count of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return count of all orders for an input order state
	 * 
	 */
	@RequestMapping(value = "countByState", method = RequestMethod.GET)
	public OrderCount getCountByState(@ModelAttribute OrderSearch orderSearch,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		orderSearch.setEmployeeId(employeeProfile.getEmployeeId());
		orderSearch.setProfileId(employeeProfile.getProfileId());
		
		return new OrderCount(orderSearch.getOrderState(), orderService.countByState(orderSearch, employeeProfile));
		
	}
	
	/**
	 * /count --> Get count of all orders
	 * 
	 *  
	 * @return If employee profile is 'DeliveryBoy', return count of orders 
	 * for an input order state assigned to the employee. If employee profile is 'Administrator',
	 * return count of all orders for an input order state
	 * 
	 */
	@RequestMapping(value = "count", method = RequestMethod.GET)
	public OrderCountList getCount(@ModelAttribute OrderSearch orderSearch,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, OrderStateNotSupportedException {
		
		final Claims claims = (Claims)request.getAttribute("claims");
		
		String claimString = claims.get("profile", String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		EmployeeProfile employeeProfile = mapper.readValue(claimString, EmployeeProfile.class);
		
		orderSearch.setEmployeeId(employeeProfile.getEmployeeId());
		orderSearch.setProfileId(employeeProfile.getProfileId());
		
		orderSearch.setOrderState(OrderService.awaitingDelivery_order_state);
		long awaitingDeliveryCount = orderService.countByState(orderSearch, employeeProfile);
		
		orderSearch.setOrderState(OrderService.allocated_order_state);
		long allocationCount = orderService.countByState(orderSearch, employeeProfile);
		
		orderSearch.setOrderState(OrderService.shipped_order_state);
		long outForDeliveryCount = orderService.countByState(orderSearch, employeeProfile);
		
		orderSearch.setOrderState(OrderService.attempted_order_state);
		long attemptedCount = orderService.countByState(orderSearch, employeeProfile);
		
		orderSearch.setOrderState(OrderService.delivered_order_state);
		orderSearch.setPaymentMode("COD");
		orderSearch.setHandoverStatus("All");
		long deliveredCount = orderService.countByState(orderSearch, employeeProfile);
		
		List<OrderCount> orderCountList = new ArrayList<OrderCount>();
		
		if(orderService.getAdminProfilesList().contains(employeeProfile.getProfileId())) {
			orderCountList.add(new OrderCount(OrderService.awaitingDelivery_order_state, awaitingDeliveryCount));
		}
		orderCountList.add(new OrderCount(OrderService.allocated_order_state, allocationCount));
		orderCountList.add(new OrderCount(OrderService.shipped_order_state, outForDeliveryCount));
		orderCountList.add(new OrderCount(OrderService.attempted_order_state, attemptedCount));
		orderCountList.add(new OrderCount(OrderService.delivered_order_state, deliveredCount));

		return new OrderCountList(orderCountList);
	}
	
	/**
	 * /assigned --> Get all orders assigned for delivery
	 *
	 * 
	 * @return List of orders assigned for delivery or empty list if no such orders are found
	 * 
	 */
	@RequestMapping(value = "assigned", method = RequestMethod.GET)
	public OrderList getAssignedOrders(HttpServletResponse response) {
		return new OrderList(orderService.getAllocatedOrders());
	}
	
	
	/**
	 * /{orderId} --> Update order state with given order state id
	 * 
	 * @return Success response if order state is update operation is successful or error response.
	 */
	@RequestMapping(value = "{orderId}", method = RequestMethod.POST)
	public void updateOrder(@PathVariable final long orderId,
			@RequestBody final OrderState orderState) throws OrderNotFoundException, OrderUpdateFailedException {
		orderService.updateOrderState(orderId, orderState);
	}
	
	/**
	 * /getDeliverySlots -> Get delivery time slots
	 * 
	 * @return List of delivery slots and ids
	 * @author amitm_000
	 *
	 */
	@RequestMapping(value = "getDeliverySlots", method = RequestMethod.GET)
	public DeliverySlotList getDeliverySlots() {
		 return new DeliverySlotList(orderService.getDeliverySlots());
	}
	
	private static class OrderList {
		@SuppressWarnings("unused")
		public List<Order> items;
		public long total;
		public int page;
		public int size;
		public int length;
		public boolean hasNext;
		public boolean hasPrev;
		
		public OrderList(List<Order> items) {
			this.items = items;
			this.total = items.size();
			this.page = 0;
			this.size = items.size();
			this.length = items.size();
			this.hasNext = false;
			this.hasPrev = false;
		}
		
		public OrderList(Page<Order> page) {
			this.items = page.getContent();
			this.total = page.getTotalElements();
			this.page = page.getNumber();
			this.size = page.getSize();
			this.length = page.getNumberOfElements();
			this.hasNext = page.hasNext();
			this.hasPrev = page.hasPrevious();
		}
	}
	
	private static class OrderCount {
		public String state;
		public long count;
		
		public OrderCount(String state, long count) {
			this.state = state;
			this.count = count;
		}
	}
	
	private static class OrderCountList {
		public List<OrderCount> items;
		
		public OrderCountList(List<OrderCount> items) {
			this.items = items;
		}
	}
	
	private static class DeliverySlotList {
		public List<DeliverySlot> items;
		
		public DeliverySlotList(List<DeliverySlot> items) {
			this.items = items;
		}
	}
	
}
