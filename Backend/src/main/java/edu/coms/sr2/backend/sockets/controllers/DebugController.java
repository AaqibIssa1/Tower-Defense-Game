package edu.coms.sr2.backend.sockets.controllers;

import java.io.IOException;

import edu.coms.sr2.backend.sockets.SocketController;
import edu.coms.sr2.backend.sockets.controllers.CommandController.SocketCommand;


/**
 * Controller class with Socket Commands for debug purposes
 * @author Nathan
 *
 */
public class DebugController {

	/**
	 * Echoes incoming message to all sockets
	 * @param body
	 * @throws IOException
	 */
	@SocketCommand
	public void echo(String body) throws IOException {
		SocketController.sendToAll(body);
	}
	
	
	public static class Foo {
		private int val;

		public Foo(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}

		public void setVal(int val) {
			this.val = val;
		}
		
	}
	
	/**
	 * Sends a simple JSON to the socket connections
	 * @throws IOException
	 */
	@SocketCommand
	public void json() throws IOException {
		SocketController.sendToAll("json", new Foo(420));
	}
}
