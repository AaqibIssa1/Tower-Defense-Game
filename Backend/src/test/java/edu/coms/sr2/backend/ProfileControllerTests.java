package edu.coms.sr2.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.coms.sr2.backend.exceptions.GeneralException;
import edu.coms.sr2.backend.http.controllers.ProfileController;
import edu.coms.sr2.backend.http.controllers.ProfileController.ProfileRegistration;
import edu.coms.sr2.backend.http.controllers.ProfileController.RegistrationResponse;
import edu.coms.sr2.backend.http.controllers.ProfileController.RegistrationResponse.Status;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileLogin;
import edu.coms.sr2.backend.http.database.ProfileRepository;
import edu.coms.sr2.backend.services.ProfileService;
import edu.coms.sr2.backend.services.ProfileService.LoginException;

public class ProfileControllerTests {
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock(name = "profiles")
	ProfileRepository mockedRepo;
	
	@Mock
	ProfileService mockedService;
	
	@InjectMocks
	ProfileController controller;
	
	@Test
	public void testCreate_goodStatus() {
		ProfileRegistration reg = new ProfileRegistration();
		reg.setProfile(new Profile());
		reg.setLogin(new ProfileLogin());
		
		Profile returnedProfile = new Profile();
		
		Mockito.when(mockedService.createProfile(reg.getProfile(), reg.getLogin())).thenReturn(returnedProfile);
		
		RegistrationResponse response = controller.create(reg);
		assertEquals(Status.GOOD, response.status);
		assertEquals(null, response.error);
		assertEquals(returnedProfile, response.newProfile);
		
	}
	
	@Test
	public void testCreate_badStatus() {
		ProfileRegistration reg = new ProfileRegistration();
		reg.setProfile(new Profile());
		reg.setLogin(new ProfileLogin());
		
		LoginException thrownException = new LoginException("Test message");
		
		Mockito.when(mockedService.createProfile(reg.getProfile(), reg.getLogin())).thenThrow(thrownException);
		
		RegistrationResponse response = controller.create(reg);
		assertEquals(Status.BAD, response.status);
		assertEquals(thrownException.getMessage(), response.error);
		assertEquals(null, response.newProfile);
	}
	
	@Test
	public void testIsOnline() {
		int onlineId = 87;
		int offlineId = 28;
		Mockito.when(mockedService.isOnline(onlineId)).thenReturn(true);
		Mockito.when(mockedService.isOnline(offlineId)).thenReturn(false);
		
		assertTrue(controller.isOnline(onlineId));
		assertFalse(controller.isOnline(offlineId));
	}
	
	private void linkServiceToRepo() {
		Mockito.when(mockedService.getProfileRepo()).thenReturn(mockedRepo);
	}
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Test
	public void testUpdateProfile_idMismatch() {
		exceptionRule.expect(GeneralException.class);
		exceptionRule.expectMessage("Path ID does not match given profile id");
		
		Profile profile = new Profile();
		controller.updateProfile(profile.getId() + 1, profile);
	}
	
	@Test
	public void testUpdateProfile_profileNotFound() {
		linkServiceToRepo();
		int id = 34;
		Profile profile = new Profile();
		profile.setId(id);
		
		Mockito.when(mockedRepo.findById(id)).thenReturn(Optional.empty());

		exceptionRule.expect(GeneralException.class);
		exceptionRule.expectMessage("Can't update non-existant profile under id: " + id);
		
		controller.updateProfile(id, profile);
	}
}
