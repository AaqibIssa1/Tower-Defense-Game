package edu.coms.sr2.backend;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.coms.sr2.backend.http.database.LoginRepository;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileLogin;
import edu.coms.sr2.backend.http.database.ProfileRepository;
import edu.coms.sr2.backend.services.ProfileService;

public class ProfileServiceTests {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock(name = "profiles") 
	ProfileRepository mockedProfileDb;

	@Mock(name = "logins") 
	LoginRepository mockedLoginDb;
	
	@InjectMocks 
	ProfileService service;
	
	//Nathan Bellows Mockito test
	@Test
	public void testLogin_valid() {
		ProfileLogin login = new ProfileLogin("test_email@test.com", "test_password", 1234);
		Profile profile = new Profile();
		profile.setId(login.getProfileId());
		profile.setUserName("aUserName");
		
		Mockito.when(mockedLoginDb.findByEmail(login.getEmail()))
			.thenReturn(Optional.of(login));
		
		Mockito.when(mockedProfileDb.findById(login.getProfileId())).thenReturn(Optional.of(profile));
		
		assertEquals(profile, service.login(login));
	}
	
}
