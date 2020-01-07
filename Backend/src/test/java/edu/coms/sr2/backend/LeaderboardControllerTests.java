package edu.coms.sr2.backend;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import edu.coms.sr2.backend.http.controllers.LeaderboardController;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileRepository;

public class LeaderboardControllerTests {
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	ProfileRepository mockedRepo;
	
	@InjectMocks
	LeaderboardController controller;
	
	private ArrayList<Profile> getRepoContents(Integer[] ranks) {
		ArrayList<Profile> profiles = new ArrayList<>();
		for(int id : ranks) {
			Profile profile = new Profile();
			profile.setCurrentRank(id);
			profiles.add(profile);
		}
		return profiles;
	}
	
	private void configureRepo(Integer[] ranks) {
		ArrayList<Profile> profiles = getRepoContents(ranks);
		Mockito.when(mockedRepo.count()).thenReturn((long) profiles.size());
		Mockito.when(mockedRepo.findAll()).thenReturn(profiles);
	}
	
	@Test
	public void testGetLeaderboard_fullLength() {
		Integer[] ranks = {1, 4, 7, 2, 6, 5};
		configureRepo(ranks);
		
		ArrayList<Integer> expected = new ArrayList<>();
		expected.addAll(Arrays.asList(ranks));
		expected.sort((a,b)->b-a);
		
		ArrayList<Integer> actual = new ArrayList<>();
		controller.getLeaderboard().forEach(profile->actual.add(profile.getCurrentRank()));
		
		assertEquals(expected, actual);
	}
	

	@Test
	public void testGetLeaderboard_truncated() {
		Integer[] ranks = {1, 8, 4, 7, 2, 6, 5, 17};
		int requestedNum = 5;
		configureRepo(ranks);
		
		List<Integer> expected = new ArrayList<>();
		expected.addAll(Arrays.asList(ranks));
		expected.sort((a,b)->b-a);
		expected = expected.subList(0, requestedNum);
		
		List<Integer> actual = new ArrayList<>();
		controller.getLeaderboard(requestedNum).forEach(profile->actual.add(profile.getCurrentRank()));
		
		assertEquals(expected, actual);
	}

}
