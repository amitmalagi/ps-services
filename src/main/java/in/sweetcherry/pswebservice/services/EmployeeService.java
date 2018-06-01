package in.sweetcherry.pswebservice.services;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.sweetcherry.pswebservice.PSWebServiceProperties;
import in.sweetcherry.pswebservice.errorhandler.EmployeeNotFoundException;
import in.sweetcherry.pswebservice.errorhandler.EmployeeProfileNotSupportedException;
import in.sweetcherry.pswebservice.errorhandler.EmployeeSigninFailedException;
import in.sweetcherry.pswebservice.models.Employee;
import in.sweetcherry.pswebservice.models.EmployeeRepository;
import in.sweetcherry.pswebservice.models.Profile;
import in.sweetcherry.pswebservice.models.ProfilePk;
import in.sweetcherry.pswebservice.models.ProfileRepository;

@Component
public class EmployeeService {

	// ------------------------
		// PRIVATE FIELDS
		// ------------------------

		private PSWebServiceProperties wsProperties;

		@Autowired
		private EmployeeRepository employeeRepository;

		@Autowired
		private ProfileRepository profileRepository;
		
		//-------------------------
		// PUBLIC FIELDS
		//-------------------------
		public static String deliveryBoy_profile = "deliveryboy";
		public static String administrator_profile = "administrator";
		public static String superAdmin_profile = "superAdmin";
		public static String admins_profile = "admins";
		public static int active_status = 1;
		
		// ------------------------
		// PUBLIC METHODS
		// ------------------------
		
		/**
		 * findById ->
		 *  	Finf an employee by a given id
		 * @param id
		 * @return An employee object or null is employee is not found
		 * 
		 */
		public Employee findById(long employeeId) {
			return employeeRepository.findOne(employeeId);
		}
		
		/**
		 * findByProfile -> 
		 * 		Return the list of employees  filtered by profile id
		 * @param profile
		 * 		The profile needed to filter employees
		 * @return The list of employees 
		 * @throws EmployeeProfileNotSupportedException 
		 */
		public List<Employee> findByProfile(String profile) throws EmployeeProfileNotSupportedException {
			ProfileId profileId = new ProfileId(profile, wsProperties);
			
			if(profile.equalsIgnoreCase(admins_profile)) {
				List<Long> adminsList = profileId.getAdminProfilesList();
			}
			
			long profileIdLong = profileId.getProfileId();
			
			if((!profile.equalsIgnoreCase(admins_profile)) && (profileIdLong == 0)) {
				throw new EmployeeProfileNotSupportedException(profile);
			}
			
			if(profile.equalsIgnoreCase(admins_profile)) 
				return employeeRepository.findByProfileIdInAndStatus(profileId.getAdminProfilesList(), active_status);
			else
				return employeeRepository.findByProfileIdAndStatus(profileIdLong, active_status);
		}
		
		/**
		 * getByEmail -> 
		 * Return the object for the employee having the passed
		 * email.
		 * 
		 * @param email
		 *            The email to search in the database.
		 * @return The employee object or throws ServletException if the employee is
		 *         not found.
		 */
		
		public Employee getByEmail(String email) throws EmployeeNotFoundException {

			if (email == null || email.isEmpty()) {
				throw new IllegalArgumentException("Email should be null or empty string.");
			}

			Employee employee;

			employee = employeeRepository.findByEmail(email);

			if (employee == null) {
				throw new EmployeeNotFoundException(email);
			}

			ProfilePk profilePk = new ProfilePk(employee.getLangId(), employee.getProfileId());
			Profile profile = profileRepository.findOne(profilePk);
			
			if (profile == null) {
				throw new EmployeeNotFoundException(email);
			}
			employee.setProfile(profile.getName());

			return employee;
		}
		
		public PSWebServiceProperties getWsProperties() {
			return wsProperties;
		}
		
		@Autowired
		public void setWsProperties(PSWebServiceProperties wsProperties) {
			this.wsProperties = wsProperties;
		}
		
		public String md5Hash(String input) throws EmployeeSigninFailedException {

			String md5 = null;

			if (null == input)
				return null;

			try {
				// Create MessageDigest object for MD5
				MessageDigest digest = MessageDigest.getInstance("MD5");

				// Update input string in message digest
				digest.update(input.getBytes(), 0, input.length());

				// Converts message digest value in base 16 (hex)
				md5 = new BigInteger(1, digest.digest()).toString(16);

			} catch (Exception e) {
				throw new EmployeeSigninFailedException("Server Error");
			}
			return md5;
		}
		
		private static class ProfileId {
			
			public String profile;
			private PSWebServiceProperties wsProperties;
			
			public ProfileId(String profile, PSWebServiceProperties wsProperties) {
				this.profile = profile;
				this.wsProperties = wsProperties;
			}
			
			public long getProfileId() {
				if(profile.equalsIgnoreCase(deliveryBoy_profile)) {
					return wsProperties.getId_deliveryboy_profile();
				} else if(profile.equalsIgnoreCase(administrator_profile)) {
					return wsProperties.getId_administrator_profile();
				} else if (profile.equalsIgnoreCase(superAdmin_profile)) {
					return wsProperties.getId_superadmin_profile();
				} else return 0;
			}
			
			public List<Long> getAdminProfilesList() {
				return Arrays.stream(wsProperties.getId_admin_profiles())
		                .boxed().collect(Collectors.<Long>toList());
			}
		}
}
