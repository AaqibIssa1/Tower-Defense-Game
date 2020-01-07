package edu.coms.sr2.backend.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.coms.sr2.backend.App;
import edu.coms.sr2.backend.exceptions.ExceptionPrinter;
import edu.coms.sr2.backend.game.Game.Listener;
import edu.coms.sr2.backend.http.database.Profile;
import edu.coms.sr2.backend.http.database.ProfileRepository;
import edu.coms.sr2.backend.services.ProfileService;
import edu.coms.sr2.backend.sockets.SocketController;
import edu.coms.sr2.backend.sockets.SocketController.ConnectionLifecycleListener;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand.Target;

@Service
public class GameService implements Listener {
	protected GameService() {}
	public static final int maxRecentPlayerHistory = 10;
	
	@Autowired
	ProfileRepository profileRepo;
	
	public static GameService getInstance() { return App.getBean(GameService.class); }
	
	private HashMap<Integer, Game> gamesPerPlayer = new HashMap<>();
	private HashMap<Integer, Game> gamesPerId = new HashMap<>();
	private HashMap<Integer, HashMap<Integer, ArrayList<Object[]>>> stashedSpectatorCommands = new HashMap<>();
	
	@SocketCommand(target = Target.SINGLETON)
	public void startNewGame(int player1, int player2) {
		if(getGameOfPlayer(player1) != null || getGameOfPlayer(player2) != null)
			return;
		
		Game game = new Game(player1, player2, this);
		while(true) {
			try {
				String command = "startGame";
				SocketController.sendToClient(player1, command);
				SocketController.sendToClient(player2, command);
				break;
			} catch(Exception e) {
				ExceptionPrinter.accept(e);
			}
		}		
		game.start();
		addDisconnectListener(game);
		updateRecentPlayers(player1, player2);
	}
	
	private void updateRecentPlayers(int player1, int player2) {
		Profile profile1 = profileRepo.findById(player1).get();
		Profile profile2 = profileRepo.findById(player2).get();
		LinkedList<Integer> recents1;
		LinkedList<Integer> recents2;
		
		try {
			recents1 = profile1.getRecentPlayerIds();
			recents2 = profile2.getRecentPlayerIds();
		} catch (IOException e) {
			ExceptionPrinter.accept(e);
			return;
		}
		
		recents1.addFirst(player2);
		recents2.addFirst(player1);
		
		while(recents1.size() > 10)
			recents1.removeLast();
		while(recents2.size() > 10)
			recents2.removeLast();
		
		try {
			profile1.updateRecentsJsonFromIds();
			profile2.updateRecentsJsonFromIds();
		} catch (JsonProcessingException e) {
			ExceptionPrinter.accept(e);
		}
		
		profileRepo.save(profile1);
		profileRepo.save(profile2);
		
	}
	
	private void addDisconnectListener(Game game) {
		ConnectionLifecycleListener listener = new ConnectionLifecycleListener() {

			@Override
			public void onDisconnect() {
				game.stop();
			}
			@Override
			public void onLogout() {
				onDisconnect();
			}
		};
		
		addDisconnectListener(game.getPlayer1(), listener);	
		addDisconnectListener(game.getPlayer2(), listener);
	}
	
	private void addDisconnectListener(int clientId, ConnectionLifecycleListener listener) {
		SocketController.getSocketControllerForClient(clientId).addConnectionListener(listener);
	}

	@Override
	public void onStart(Game game) {
		System.out.println("Starting game " + game.getGameId() + 
				" with clients " + game.getPlayer1() + " and " + game.getPlayer2());
		gamesPerPlayer.put(game.getPlayer1(), game);
		gamesPerPlayer.put(game.getPlayer2(), game);
		gamesPerId.put(game.getGameId(), game);
		
		HashMap<Integer, ArrayList<Object[]>> argsPerPerspective = new HashMap<>();
		argsPerPerspective.put(game.getPlayer1(), new ArrayList<>(200));
		argsPerPerspective.put(game.getPlayer2(), new ArrayList<>(200));
		stashedSpectatorCommands.put(game.getGameId(), argsPerPerspective);
	}

	@Override
	public void onStop(Game game) {
		gamesPerPlayer.remove(game.getPlayer1());
		gamesPerPlayer.remove(game.getPlayer2());
		gamesPerId.remove(game.getGameId(), game);
		stashedSpectatorCommands.remove(game.getGameId());
		
		try {
			SocketController.trySendToClients(game.getPlayersAndSpectators(), "stopGame");
		} catch (IOException e) {
			ExceptionPrinter.accept(e);
		}
	}
	
	public Game getGameById(int gameId) {
		return gamesPerId.get(gameId); 
	}
	
	public Game getGameOfPlayer(int playerId) {
		return gamesPerPlayer.get(playerId);
	}
	
	public int getOpponent(int playerId) {
		Game game = getGameOfPlayer(playerId);
		return game == null? -1 : game.getOpponent(playerId);
	}
	
	@SocketCommand(target = Target.SINGLETON) 
	public void startSpectating(int gameId, int perspectivePlayerId) {
		getGameById(gameId).addSpectator(SocketController.getThreadClientId(), perspectivePlayerId);
		fastForwardFromStash(gameId, perspectivePlayerId);
	}
	
	private void fastForwardFromStash(int gameId, int perspectivePlayerId) {
		for(Object[] command : stashedSpectatorCommands.get(gameId).get(perspectivePlayerId))
			SocketController.getCurrentSocketController().send(command);
	}
	
	@SocketCommand(target = Target.SINGLETON) 
	public void stopSpectating(int gameId) {
		System.out.println("STOP SPECTATING");
		getGameById(gameId).removeSpectator(SocketController.getThreadClientId());
	}
	
	@SocketCommand(target = Target.SINGLETON)
	public void stopGame() {
		Game game = getGameOfPlayer(SocketController.getThreadClientId());
		if(game != null)
			game.stop();
	}
	
	@SocketCommand(target = Target.SINGLETON)
	public void gameWon() throws IOException {
		int clientId = SocketController.getThreadClientId();
		Game game = getGameOfPlayer(clientId);
		
		ProfileRepository profileRepo = ProfileService.getInstance().getProfileRepo();
		Profile winner = profileRepo.findById(clientId).get();
		Profile loser = profileRepo.findById(game.getOpponent(clientId)).get();
		
		SocketController.sendToClients(game.getAllSpectators(), "specGameOver", 
				winner.getUserName());
		
		updateProfiles(winner, loser);
	}
	
	private void updateProfiles(Profile winner, Profile loser) {
		double winnerRank = Math.max(1, winner.getCurrentRank());
		double loserRank = Math.max(1, loser.getCurrentRank());
		
		double winnerGain = 10 * (1 + Math.min(3, loserRank / winnerRank));
		double loserLoss = 5 * (1 + Math.min(2, loserRank / winnerRank));

		winnerRank += winnerGain;
		loserRank = Math.max(1, loserRank - loserLoss);
		
		winner.setCurrentRank((int) winnerRank);
		loser.setCurrentRank((int) loserRank);
		
		winner.setHighestRank(Math.max(winner.getCurrentRank(), winner.getHighestRank()));
		loser.setLowestRank(Math.min(loser.getCurrentRank(), winner.getLowestRank()));
		
		winner.setWins(winner.getWins() + 1);
		loser.setLosses(loser.getLosses() + 1);
		
		ProfileRepository profileRepo = ProfileService.getInstance().getProfileRepo();
		profileRepo.save(winner);
		profileRepo.save(loser);
	}
	
	public void stashSpectatorCommand(Game game, int perspectivePlayerId, Object[] args) {
		stashedSpectatorCommands.get(game.getGameId()).get(perspectivePlayerId).add(args);
	}
}
