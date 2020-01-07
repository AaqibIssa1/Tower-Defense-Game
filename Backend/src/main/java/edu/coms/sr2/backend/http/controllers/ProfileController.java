package edu.coms.sr2.backend.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.coms.sr2.backend.exceptions.GeneralException;
import edu.coms.sr2.backend.http.controllers.ProfileController.RegistrationResponse.Status;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileLogin;
import edu.coms.sr2.backend.services.ProfileService;
import edu.coms.sr2.backend.services.ProfileService.LoginException;


/**
 * HTTP Controller for profile-related parts of the REST API
 * @author Nathan
 *
 */

@RestController
public class ProfileController 
{
	@Autowired
	ProfileService service;

	/**
	 * Gets all the profiles in the database
	 * @return Iterable collection of the profiles
	 */
	@GetMapping("profiles")
	public Iterable<Profile> getAllProfiles() {
		return service.getProfileRepo().findAll();
	}
	
	/**
	 * Gets a profile from the database by ID
	 * @param id The ID of the desired profile
	 * @return The corresponding profile (or a NoSuchElementException is thrown)
	 */
	@GetMapping("profiles/{id}")
	public Profile getProfile(@PathVariable int id) {
		return service.getProfileRepo().findById(id).get();
	}
	
	/**
	 * Method/endpoint to poll for a profile/user's online status
	 * @param id
	 * @return True if the player is online on some device
	 */
	@GetMapping("profiles/{id}/online")
	public boolean isOnline(@PathVariable int id) {
		return service.isOnline(id);
	}
	
	/**
	 * Updates a profile in the database via a POST at the appropriate "profiles/{id}" endpoint
	 * @param id The id to update
	 * @param profile The profile object posted
	 * @return The resulting (given) profile
	 */
	@PostMapping("profiles/{id}")
	public Profile updateProfile(@PathVariable int id, @RequestBody Profile profile) {
		if(id != profile.getId())
			throw new GeneralException("Path ID does not match given profile id");
		
		if(service.getProfileRepo().findById(profile.getId()).isPresent())
			return service.getProfileRepo().save(profile);
		else
			throw new GeneralException("Can't update non-existant profile under id: " + profile.getId());
	}
	
	/**
	 * Inner class to encapsulate the data structure of an incoming new-profile registration
	 * @author Nathan
	 *
	 */
	public static class ProfileRegistration
	{
		private Profile profile;
		private ProfileLogin login;
		public Profile getProfile() {
			return profile;
		}
		public void setProfile(Profile profile) {
			this.profile = profile;
		}
		public ProfileLogin getLogin() {
			return login;
		}
		public void setLogin(ProfileLogin login) {
			this.login = login;
		}
	}
	
	public static class RegistrationResponse {
		public static enum Status { GOOD, BAD };
		public Status status;
		public String error;
		public Profile newProfile;
	}

	/**
	 * Attempts to register a new profile
	 * @param reg The profile-registration data to create a new profile on
	 * @return The created profile
	 */
	@PostMapping("profiles")
	public RegistrationResponse create(@RequestBody ProfileRegistration reg) {
		RegistrationResponse response = new RegistrationResponse();
		try {
			response.newProfile = service.createProfile(reg.getProfile(), reg.getLogin());
			response.status = Status.GOOD; 
		}
		catch(LoginException e) {
			response.status = Status.BAD;
			response.error = e.getMessage();
		}
		return response;
	}
	
}
