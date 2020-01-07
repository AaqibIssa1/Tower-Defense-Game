package edu.coms.sr2.backend.http.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * POJO class to encapsulate login-attempt data structure. 
 * Integrated with the corresponding login database table "profile_logins"
 * @author Nathan
 *
 */

@Entity
@Table(name = "profile_logins")
public class ProfileLogin 
{
	@Id
	private String email;
	private String password;
	@Column(name = "profile_id")
	private int profileId;
	
	public ProfileLogin() {}
	
	public ProfileLogin(String email, String password, int profileId) 
	{
		this.email = email;
		this.password = password;
		this.profileId = profileId;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	
}
