package in.sweetcherry.pswebservice.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	/**
	   * Return the employee having the passed email or null if no employee is found.
	   * 
	   * @param email the employee email.
	   */
	public Employee findByEmail(String email);
	public List<Employee> findByProfileIdAndStatus(long profileId, int status);
	public List<Employee> findByProfileIdInAndStatus(List<Long> profileIdList, int status);
}
