package edu.coms.sr2.backend.http.controllers;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileRepository;

@RestController
public class LeaderboardController {
	
	@Autowired
	ProfileRepository profileRepo;
	
	@GetMapping("leaderboard")
	public Iterable<Profile> getLeaderboard() {
		return getLeaderboard((int) profileRepo.count());
	}
	
	@GetMapping("leaderboard/{numProfiles}")
	public Iterable<Profile> getLeaderboard(@PathVariable int numProfiles) {
		ArrayList<Profile> allProfiles = new ArrayList<Profile>((int) profileRepo.count());
		
		for(Profile profile : profileRepo.findAll())
			allProfiles.add(profile);
		
		allProfiles.sort((profile1, profile2)->
			profile2.getCurrentRank() - profile1.getCurrentRank());

		return allProfiles.subList(0, numProfiles);
	}

}
