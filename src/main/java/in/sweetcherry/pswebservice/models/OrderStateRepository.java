package in.sweetcherry.pswebservice.models;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

public interface OrderStateRepository extends CrudRepository<OrderStateId, Long> {

	@Cacheable(value = "orderStates")
    public OrderStateId findOne(Long id);
	
	//@Cacheable(value = "orderStates")
	//Iterable<OrderStateId> findAll();
}
