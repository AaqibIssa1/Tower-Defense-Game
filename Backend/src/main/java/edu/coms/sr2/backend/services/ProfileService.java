package edu.coms.sr2.backend.services;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.coms.sr2.backend.App;
import edu.coms.sr2.backend.exceptions.GeneralException;
import edu.coms.sr2.backend.http.database.LoginRepository;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileLogin;
import edu.coms.sr2.backend.http.database.ProfileRepository;


/**
 * Service class to provide profile and login-related functions
 * @author Nathan
 *
 */
@Service
public class ProfileService 
{
	@Autowired
	private ProfileRepository profiles;
	
	@Autowired
	private LoginRepository logins;
	
	private HashSet<Integer> onlineProfiles = new HashSet<>();
	
	public ProfileRepository getProfileRepo() {
		return profiles;
	}

	public LoginRepository getLoginRepo() {
		return logins;
	}
	
	public static ProfileService getInstance() {
		return App.getBean(ProfileService.class);
	}
	
	public void setOnline(int profileId) {
		onlineProfiles.add(profileId);
	}
	
	public void setOffline(int profileId) {
		onlineProfiles.remove(profileId);
	}
	
	public boolean isOnline(int profileId) {
		return onlineProfiles.contains(profileId);
	}


	/**
	 * Inner exception class for login failures, for inheritance purposes
	 * @author Nathan
	 *
	 */
	public static class LoginException extends GeneralException 
	{
		private static final long serialVersionUID = -8646706806487238757L;
		private String message;
		
		public LoginException(String message) { this.message = message; }
		public LoginException() { this(null); }
		
		@Override
		public String getMessage() { return message; }
	}
	
	/**
	 * Inner exception class for failed login due to an unregistered email being used
	 * @author Nathan
	 *
	 */
	public static class EmailNotRegisteredException extends LoginException 
	{
		private static final long serialVersionUID = -4279426394335886513L;
		public String email;
		public EmailNotRegisteredException() {}
		public EmailNotRegisteredException(String email) {
			this.email = email;
		}
		@Override 
		public String getMessage() {
			return "No profile exists under the given email address" + (email == null? "." : ": " + email);
		}
	}
	
	/**
	 * Inner exception class for failed login due to an incorrect password (for the given email)
	 * @author Nathan
	 *
	 */
	public static class BadPasswordException extends LoginException {
		private static final long serialVersionUID = 2799144361446697597L;

		@Override
		public String getMessage() {
			return "Password is incorrect.";
		}
	}

	/**
	 * Asserts that a given ProfileLogin is of proper structure (no null/empty fields)
	 * @param creds ProfileLogin object to check
	 */
	private void checkValidity(ProfileLogin creds) {
		if(creds.getEmail() == null || creds.getEmail().isEmpty())
			throw new LoginException("Email cannot be null.");
		
		if(creds.getPassword() == null || creds.getPassword().isEmpty())
			throw new LoginException("Password cannot be null.");
		
		if(!creds.getEmail().matches("\\w+@\\w+\\.\\w+"))
			throw new LoginException("Invalid email address");
	}
	
	/**
	 * Attempts a login with the given login-credentials. If successful, returns the corresponding profile,
	 * otherwise throws an appropriate exception
	 * @param creds Login credentials to login with
	 * @return The corresponding profile
	 */
	public Profile login(ProfileLogin creds)
	{
		checkValidity(creds);
		
		Optional<ProfileLogin> login = logins.findByEmail(creds.getEmail());
		if(!login.isPresent())
			throw new EmailNotRegisteredException(creds.getEmail());
		
		if(!creds.getPassword().equals(login.get().getPassword()))
			throw new BadPasswordException();			
		
		int profileId = login.get().getProfileId();
		if(isOnline(profileId)) 
			throw new LoginException("Profile is already online on another device.");
		
		return profiles.findById(profileId).get();
	}
	
	/**
	 * Inner exception for a registration attempt that uses an already-registered email
	 * @author Nathan
	 *
	 */
	public static class DuplicateRegistrationException extends GeneralException 
	{
		private static final long serialVersionUID = -5764641678254657232L;
		public String email;
		public DuplicateRegistrationException() {}
		public DuplicateRegistrationException(String email) {
			this.email = email;
		}
		@Override
		public String getMessage() {
			return "There is already a profile with the given email" + (email == null? "." : ": " + email);
		}
	}
	
	/**
	 * Carries out a registration attempt
	 * @param profile Any profile information to be transferred/included in the generated profile
	 * @param login The login to link to the given profile
	 * @return The created profile (with a generated ID)
	 */
	public Profile createProfile(Profile profile, ProfileLogin login)
	{
		checkValidity(login);
		if(logins.findByEmail(login.getEmail()).isPresent())
			throw new DuplicateRegistrationException(login.getEmail());
		
		profile.setId(0);
		profile.setCurrentRank(1);
		profile.setLowestRank(1);
		profile.setHighestRank(1);
		profile.setLosses(0);
		profile.setWins(0);
		
		Profile result = profiles.save(profile);
		logins.save(login);
		
		return result;
	}
	
}