package in.sweetcherry.pswebservice.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ps_profile_lang")
public class Profile {

	
	@EmbeddedId
	private ProfilePk profileId;
	
	@NotNull
	private String name;

	public Profile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Profile(String name) {
		super();
		this.name = name;
	}

	

	public ProfilePk getProfileId() {
		return profileId;
	}

	public void setProfileId(ProfilePk profileId) {
		this.profileId = profileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}


