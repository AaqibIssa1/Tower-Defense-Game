package edu.coms.sr2.backend.http.database;


import java.io.IOException;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.coms.sr2.backend.App;

/**
 * POJO class to encapsulate data structure of a profile. 
 * Integrated with the corresponding database table "profiles"
 * @author Nathan
 *
 */

@Entity
@Table(name = "profiles")
public class Profile 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "real_name")
	private String realName;
	@Column(name = "current_rank")
	private int currentRank;
	@Column(name = "highest_rank")
	private int highestRank;
	@Column(name = "lowest_rank")
	private int lowestRank;
	@Column(name = "wins")
	private int wins;
	@Column(name = "losses")
	private int losses;
	@Column(name = "friend_list")
	@JsonIgnore
	private String friendListJson;
	@Column(name = "recent_players")
	@JsonIgnore
	private String recentPlayersJson;
	@Transient
	private int[] friendListIds;
	@Transient
	private LinkedList<Integer> recentPlayerIds;
	
	public Profile() { }
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}

	public int getHighestRank() {
		return highestRank;
	}

	public void setHighestRank(int highestRank) {
		this.highestRank = highestRank;
	}

	public int getLowestRank() {
		return lowestRank;
	}

	public void setLowestRank(int lowestRank) {
		this.lowestRank = lowestRank;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public String getFriendListJson() throws JsonProcessingException {
		updateFriendJsonFromIds();
		return friendListJson;
	}
	
	public void setFriendListJson(String friendListJson) throws JsonParseException, JsonMappingException, IOException {
		this.friendListJson = friendListJson;
		updateFriendIdsFromJson();
	}

	public int[] getFriendListIds() throws JsonParseException, JsonMappingException, IOException {
		if(friendListIds == null) 
			updateFriendIdsFromJson();
		return friendListIds;
	}

	public void setFriendListIds(int[] friendListIds) throws JsonProcessingException {
		this.friendListIds = friendListIds;
		updateFriendJsonFromIds();
	}
	
	public void updateFriendJsonFromIds() throws JsonProcessingException {
		this.friendListJson = App.getJsonMapper().writeValueAsString(friendListIds);
	}
	
	public void updateFriendIdsFromJson() throws JsonParseException, JsonMappingException, IOException  {
		this.friendListIds = friendListJson == null || friendListJson.isEmpty() ? 
				new int[0] :
				App.getJsonMapper().readValue(friendListJson, int[].class);
	}
	

	public String getRecentPlayersJson() throws JsonProcessingException {
		updateRecentsJsonFromIds();
		return recentPlayersJson;
	}
	
	public void setRecentPlayersJson(String json) throws JsonParseException, JsonMappingException, IOException {
		this.recentPlayersJson = json;
		updateRecentIdsFromJson();
	}

	public LinkedList<Integer> getRecentPlayerIds() throws JsonParseException, JsonMappingException, IOException  {
		if(recentPlayerIds == null) 
			updateRecentIdsFromJson();
		return recentPlayerIds;
	}

	public void setRecentPlayerIds(LinkedList<Integer> ids) throws JsonProcessingException {
		this.recentPlayerIds = ids;
		updateRecentsJsonFromIds();
	}
	
	public void updateRecentsJsonFromIds() throws JsonProcessingException {
		this.recentPlayersJson = App.getJsonMapper().writeValueAsString(recentPlayerIds);
	}
	
	public void updateRecentIdsFromJson() throws JsonParseException, JsonMappingException, IOException  {
		this.recentPlayerIds = recentPlayersJson == null || recentPlayersJson.isEmpty() ? 
				new LinkedList<>() :
				App.getJsonMapper().readValue(recentPlayersJson, new TypeReference<LinkedList<Integer>>() { });
	}
	
}
