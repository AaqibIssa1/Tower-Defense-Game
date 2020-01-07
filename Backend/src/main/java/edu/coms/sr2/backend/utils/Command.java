package edu.coms.sr2.backend.utils;

/**
 * Simple POJO wrapper for a command
 * @author Nathan
 *
 */
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
