package edu.coms.sr2.backend.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.coms.sr2.backend.exceptions.ExceptionPrinter;
import edu.coms.sr2.backend.sockets.SocketController;
import edu.coms.sr2.backend.sockets.SocketController.ConnectionLifecycleListener;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand;

public class MatchmakingService 
{
	private static Queue<Integer> queue = new PriorityQueue<>();
	private static HashMap<Integer, HashSet<Integer>> matchInvites = new HashMap<>();

	@SocketCommand("findMatch")
	public static void addToQueue() throws IOException {
		addToQueue(SocketController.getThreadClientId());
	}

	public static void addToQueue(int playerId) throws IOException {
		SocketController.trySendToClient(playerId, "findingMatch");
		if(!queue.contains(playerId))
		{
			if(queue.isEmpty()) {
				queue.add(playerId);
				SocketController.getCurrentSocketController().addConnectionListener(new ConnectionLifecycleListener() {
					@Override
					public void onDisconnect() {
						try {
							removeFromQueue(playerId);
						} catch (IOException e) {
							ExceptionPrinter.accept(e);
						}
					}
					@Override
					public void onLogout() {
						onDisconnect();
					}
				});
			}
			else {
				SocketController.sendToClient(playerId, "stopFindingMatch");
				GameService.getInstance().startNewGame(playerId, queue.remove());
			}
		}
	}

	@SocketCommand("stopFindingMatch")
	public static void removeFromQueue() throws IOException {
		removeFromQueue(SocketController.getThreadClientId());
	}

	public static void removeFromQueue(int playerId) throws IOException {
		queue.remove(playerId);
		SocketController.trySendToClient(playerId, "stopFindingMatch");
	}

	@SocketCommand
	public static void sendMatchInvite(int playerId) throws IOException {
		int sender = SocketController.getThreadClientId();
		matchInvites.computeIfAbsent(sender, id-> {
			addDisconnectListener_invites(sender);
			return new HashSet<>();
		}).add(playerId);
		SocketController.trySendToClient(playerId, "incomingMatchInvite", sender);
	}

	public static void addDisconnectListener_invites(int sender) {
		SocketController.getCurrentSocketController().addConnectionListener(new ConnectionLifecycleListener() {
			@Override
			public void onDisconnect() {
				HashSet<Integer> invites = matchInvites.get(sender);
				if(invites != null)
					for(Integer reciever : invites)
						try {
							SocketController.trySendToClient(reciever, "cancelMatchInvite", sender);
						} catch (Exception e) {
							ExceptionPrinter.accept(e);
						}
				matchInvites.remove(sender);
			}
			@Override
			public void onLogout() {
				onDisconnect();
			}
		});
	}

	@SocketCommand
	public static void cancelMatchInvite(int playerId)  {
		cancelMatchInvite(SocketController.getThreadClientId(), playerId);
	}

	public static void cancelMatchInvite(int sender, int reciever) {
		HashSet<Integer> invites = matchInvites.get(sender);
		if(invites != null)
			invites.remove(reciever);
		try {
			SocketController.trySendToClient(reciever, "cancelMatchInvite", sender);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
