package in.sweetcherry.pswebservice.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long> {

	public List<OrderDetail> findByOrderId(long orderId);
}
