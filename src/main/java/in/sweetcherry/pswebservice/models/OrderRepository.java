package in.sweetcherry.pswebservice.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {

	public List<Order> findByOrderStateId(long orderStateId);

	public List<Order> findByOrderStateIdNotOrderByDeliveryDateAsc(long orderStateId);

	public List<Order> findByOrderStateIdAndEmployeeId(long orderStateId, long employeeId);

	public Page<Order> findByOrderStateIdOrderByDeliveryDateAsc(long orderStateId, Pageable pageable);
	public Page<Order> findByOrderStateIdAndDeliveryDateBetweenOrderByDeliveryDateAsc(long orderStateId, Date fromDate,
			Date toDate, Pageable pageable);

	public Page<Order> findByOrderStateIdNotOrderByDeliveryDateAsc(long orderStateId, Pageable pageable);
	public Page<Order> findByOrderStateIdNotAndDeliveryDateBetweenOrderByDeliveryDateAsc(
			long orderStateId, Date fromDate, Date toDate, Pageable pageable);
	
	public Page<Order> findByOrderStateIdAndEmployeeIdOrderByDeliveryDateAsc(long orderStateId, long employeeId,
			Pageable pageable);
	public Page<Order> findByOrderStateIdAndEmployeeIdAndDeliveryDateBetweenOrderByDeliveryDateAsc(long orderStateId, long employeeId,
			Date fromDate, Date toDate, Pageable pageable);
	
	public Page<Order> findByOrderStateIdInOrderByDeliveryDateAsc(List<Long> orderStateIdList, Pageable pageable);
	public Page<Order> findByOrderStateIdInAndDeliveryDateBetweenOrderByDeliveryDateAsc(List<Long> orderStateIdList, Date fromDate,
			Date toDate, Pageable pageable);

	public long countByOrderStateId(long orderStateId);
	
	public long countByOrderStateIdIn(List<Long> orderStateIdList);
	
	public long countByOrderStateIdInAndDeliveryDateBetween(List<Long> orderStateIdList, Date fromDate, Date toDate);
	
	public long countByOrderStateIdAndDeliveryDateBetween(long orderStateId, Date fromDate, Date toDate);

	public long countByEmployeeId(long employeeId);

	public long countByOrderStateIdAndEmployeeId(long orderStateId, long employeeId);
	
	public long countByOrderStateIdAndEmployeeIdAndDeliveryDateBetween(long orderStateId, long employeeId, Date fromDate, Date toDate);
}
