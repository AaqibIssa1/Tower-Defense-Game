package edu.coms.sr2.game.sockets.controllers;

import java.io.IOException;

import edu.coms.sr2.game.sockets.SocketController;

public class DebugController extends CommandController {
    @SocketCommand
    public void echo(String body) throws IOException {
        SocketController.send(body);
    }

    public static class Foo {
        private int val;
        public Foo(int val) {
            this.val = val;
        }
    }

    @SocketCommand
    public void json() throws IOException {
        SocketController.send("json",new Foo(420));
    }
}
