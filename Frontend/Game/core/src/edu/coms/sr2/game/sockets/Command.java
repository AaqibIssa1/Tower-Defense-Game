package edu.coms.sr2.game.sockets;

public abstract class Command {
    protected String name;
    protected Object[] params;

    protected Command(String name, Object... params) {
        this.name = name;
        this.params = params;
    }

    public String getName() { return name; }
    public Object[] getParams() { return params; }
}
