package in.sweetcherry.pswebservice.models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProfilePk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id_lang;
	private long id_profile;
	
	public ProfilePk() {
		
	}
	
	public ProfilePk(long id_lang, long id_profile) {
		this.id_lang = id_lang;
		this.id_profile = id_profile;
	}

	public long getId_lang() {
		return id_lang;
	}

	public void setId_lang(long id_lang) {
		this.id_lang = id_lang;
	}

	public long getId_profile() {
		return id_profile;
	}

	public void setId_profile(long id_profile) {
		this.id_profile = id_profile;
	}
	
	
}