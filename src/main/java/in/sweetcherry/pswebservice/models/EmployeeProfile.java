package in.sweetcherry.pswebservice.models;

public class EmployeeProfile {
	public long employeeId;
	public String email;
	public String firstname;
	public String lastname;
	public long profileId;
	public String profile;
	
	public EmployeeProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeProfile(long employeeId, String email, String firstname, String lastname, long profileId, String profile) {
		super();
		this.employeeId = employeeId;
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.profileId = profileId;
		this.profile = profile;
	}
	
	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	
}