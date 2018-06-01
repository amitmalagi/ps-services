package in.sweetcherry.pswebservice.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import in.sweetcherry.pswebservice.PSWebServiceProperties;
import in.sweetcherry.pswebservice.services.OrderService;

public class OrderSpecification implements Specification<Order> {
    private OrderSearch criteria;
    private PSWebServiceProperties wsProperties;


    public OrderSpecification(OrderSearch orderSearch, PSWebServiceProperties wsProperties) {
        this.criteria = orderSearch;
        this.wsProperties = wsProperties;
    }
    
    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, 
            CriteriaBuilder cb) {
    	
    	OrderStateId orderState = new OrderStateId(criteria.getOrderState(), wsProperties);
    	
    	Path<Long> orderStateId = root.get("orderStateId");
    	Path<Long> employeeId = root.get("employeeId");
    	Path<Date> deliveryDate = root.get("deliveryDate");
    	Path<String> deliveredDetails = root.get("deliveredDetails");
    	Path<String> handoverDetails = root.get("handoverDetails");
    	
    	final List<Predicate> predicates = new ArrayList<Predicate>();
    	
    	if(orderState.getOrderState().equalsIgnoreCase(OrderService.awaitingDelivery_order_state)) {
    		List<Long> orderStateIdList = orderState.getAwaitingDeliveryOrderStatesList();
    		predicates.add(orderStateId.in(orderStateIdList));
    	}
    	
    	if(criteria.getProfileId() == wsProperties.getId_deliveryboy_profile()) {
    		predicates.add(cb.equal(employeeId, criteria.getEmployeeId()));
    	}
    	
    	if(orderState.getOrderStateId() != 0) {
    		predicates.add(cb.equal(orderStateId, orderState.getOrderStateId()));
    	}
    	
    	if((criteria.getFromDate() != null) && (criteria.getToDate() != null)) {
    	    predicates.add(cb.between(deliveryDate, criteria.getFromDate(), criteria.getToDate()));
    	}
    	
    	if((criteria.getPaymentMode() != null) && (!criteria.getPaymentMode().equalsIgnoreCase("All"))) {
    	    predicates.add(cb.like(deliveredDetails, "%"+criteria.getPaymentMode()+"%"));	
    	}
    	
    	if((criteria.getHandoverStatus() != null) && (!criteria.getHandoverStatus().equalsIgnoreCase("All"))) {
    		predicates.add(cb.like(handoverDetails, "%"+criteria.getHandoverStatus()+"%"));
    	}
    	
    	query.orderBy(cb.asc(deliveryDate));
    	return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
