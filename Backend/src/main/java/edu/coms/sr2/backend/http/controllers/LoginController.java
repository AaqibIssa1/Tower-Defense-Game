package edu.coms.sr2.backend.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileLogin;
import edu.coms.sr2.backend.services.ProfileService;


/**
 * Controller for elements of the REST API that involve user login
 * @author Nathan
 *
 */

@RestController
public class LoginController 
{
	@Autowired
	ProfileService service;

	/**
	 * Inner class to encapsulate the response to a login attempt
	 * @author Nathan
	 *
	 */
	public static class LoginResponse
	{	
		public static enum Status	{ GOOD, BAD }
		
		public Status status;
		public String error;
		public int authToken;
		public int profileId;
		
		public LoginResponse(int authToken, int profileId)
		{
			status = Status.GOOD;
			error = "";
			this.authToken = authToken;
			this.profileId = profileId;
		}
		
		public LoginResponse(String error) 
		{
			status = Status.BAD;
			this.error = error;
			authToken = -1;
			profileId = -1;
		}
	}

	/**
	 * Handles incoming login POST requests (at /login) and gives the proper response
	 * @param creds The incoming login credentials
	 * @return A LoginResponse object representing the response
	 */
	@PostMapping("/login")
	public LoginResponse login(@RequestBody ProfileLogin creds)
	{
		try 
		{
			Profile profile = service.login(creds);
			return new LoginResponse(0, profile.getId());
		} 
		catch(ProfileService.LoginException e) 
		{
			return new LoginResponse(e.getMessage());
		}
	}
	

}


