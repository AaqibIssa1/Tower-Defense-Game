package edu.coms.sr2.backend;

import java.io.IOException;
import java.util.ArrayList;

import edu.coms.sr2.backend.game.Game;
import edu.coms.sr2.backend.game.GameService;
import edu.coms.sr2.backend.sockets.SocketController;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand;
import edu.coms.sr2.backend.utils.ScreenCoordinate;


public class SimpleGameCommands {
	
	private void sendToSpectators(Game game, int perspectivePlayerId, Object[] args, boolean stash) throws IOException {
		if(stash)
			GameService.getInstance().stashSpectatorCommand(game, perspectivePlayerId, args);
		SocketController.sendToClients(game.getSpectators(perspectivePlayerId), args);
	}
	
	private void sendToOthers(String spectatorCommandPrefix, boolean stashForSpectators, String opponentCommand, Object...args) throws IOException {
		
		int clientId = SocketController.getThreadClientId();
		Game game = GameService.getInstance().getGameOfPlayer(clientId);
		int opponentId = game.getOpponent(clientId);
		
		ArrayList<Object> selfArgsList = new ArrayList<>();
		ArrayList<Object> enemyArgsList = new ArrayList<>();
		selfArgsList.add(null);
		enemyArgsList.add(null);
		
		for(Object arg : args) {
			if(arg instanceof ScreenCoordinate) {
				ScreenCoordinate coord = (ScreenCoordinate) arg;
				
				selfArgsList.add(coord.getX());
				selfArgsList.add(coord.getY());
				
				enemyArgsList.add(coord.getXPrime());
				enemyArgsList.add(coord.getYPrime());
			}
			else {
				selfArgsList.add(arg);
				enemyArgsList.add(arg);
			}
		}

		Object[] selfArgs = selfArgsList.toArray();
		Object[] enemyArgs = enemyArgsList.toArray();
		
		enemyArgs[0] = opponentCommand;
		SocketController.sendToClient(opponentId, enemyArgs);
		
		selfArgs[0] = spectatorCommandPrefix + "_self";
		enemyArgs[0] = spectatorCommandPrefix + "_enemy";
		sendToSpectators(game, clientId, selfArgs, stashForSpectators);
		sendToSpectators(game, opponentId, enemyArgs, stashForSpectators);
		
	}
	
	@SocketCommand("arrow")
	public void arrow(float xPos, float yPos, float xDes, float yDes, int id) throws IOException {
		sendToOthers("specArrow", false, "othersArrow", new ScreenCoordinate(xPos, yPos), new ScreenCoordinate(xDes, yDes), id);
	}
	
	@SocketCommand("buildingCreated")
	public void buildingCreated(float xPos, float yPos, int id, int type) throws IOException {
		System.out.println("Building created by player" + SocketController.getThreadClientId());
		sendToOthers("specBuilding", true, "othersBuilding", new ScreenCoordinate(xPos, yPos), id, type);
	}
	
	@SocketCommand("removeArrow")
	public void removeArrow(int id) throws IOException {
		sendToOthers("specRemoveArrow", false, "othersRemoveArrow", id);
	}
	
	@SocketCommand("destroyBuilding")
	public void destroyBuilding(int id) throws IOException {
		sendToOthers("specDestroyBuilding", true, "selfDestroyBuilding", id);
	}
	
	@SocketCommand
	public void damageEnemyTower() throws IOException {
		sendToOthers("specDamageTower", true, "damageTower");
	}
	
	
}
