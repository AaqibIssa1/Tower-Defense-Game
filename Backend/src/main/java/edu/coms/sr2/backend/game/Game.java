package edu.coms.sr2.backend.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import edu.coms.sr2.backend.exceptions.GeneralException;

public class Game {
	private static int idGen = 0;
	private int gameId = idGen++;
	private int player1, player2;
	private HashSet<Integer> spectators_P1 = new HashSet<>();
	private HashSet<Integer> spectators_P2 = new HashSet<>();
	
	public interface Listener {
		void onStart(Game game);
		void onStop(Game game);
	}
	private Listener listener;
	
	public Game(int player1, int player2, Listener listener) {
		this.player1 = player1;
		this.player2 = player2;
		this.listener = listener;
	}
	
	public void start() {
		listener.onStart(this);
		
	}
	
	public void stop() {
		listener.onStop(this);
	}
	
	public int getPlayer1() { return player1; }
	public int getPlayer2() { return player2; }
	

	
	public int getGameId() { return gameId; }
	
	public int getOpponent(int playerId) {
		return playerId == player1? player2 : player1;
	}
	
	public int getPlayerNumber(int playerId) {
		if(playerId == player1)
			return 1;
		else if(playerId == player2)
			return 2;
		else return -1;
	}
	
	public boolean addSpectator(int spectatorId, int perspectivePlayerId) 
	{
		return getSpectators(perspectivePlayerId).add(spectatorId);
	}
	
	public void removeSpectator(int spectatorId) {
		spectators_P1.remove(spectatorId);
		spectators_P2.remove(spectatorId);
	}
	
	public Collection<Integer> getPlayers() {
		return Arrays.asList(player1, player2);
	}
	
	public Collection<Integer> getPlayer1Spectators() {
		return spectators_P1;
	}
	
	public Collection<Integer> getPlayer2Spectators() {
		return spectators_P2;
	}
	
	public Collection<Integer> getAllSpectators() {
		ArrayList<Integer> spectators = new ArrayList<>(spectators_P1);
		spectators.addAll(spectators_P2);
		return spectators;
	}
	
	public Collection<Integer> getPlayersAndSpectators() {
		ArrayList<Integer> list = new ArrayList<>();
		list.add(player1);
		list.add(player2);
		list.addAll(spectators_P1);
		list.addAll(spectators_P2);
		return list;
	}
	
	
	public Collection<Integer> getSpectators(int perspectivePlayerId) {
		switch(getPlayerNumber(perspectivePlayerId)) {
		case 1:
			return spectators_P1;
		case 2:
			return spectators_P2;
		default: 
			throw new GeneralException(
					"Profile " + perspectivePlayerId + 
					" is not a player in the spectated game.");
		}
	}
}
