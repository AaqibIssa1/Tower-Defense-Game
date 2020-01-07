package edu.coms.sr2.game.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import edu.coms.sr2.game.Application;

public class BetterOutputStream extends DataOutputStream {
    public BetterOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void write(Object o) throws IOException {
        if(o instanceof Integer) writeInt((Integer) o);
        else if(o instanceof Short) writeShort((Short) o);
        else if(o instanceof Long) writeLong((Long) o);
        else if(o instanceof Float) writeFloat((Float) o);
        else if(o instanceof Double) writeDouble((Double) o);
        else if(o instanceof Boolean) writeBoolean((Boolean) o);
        else if(o instanceof String) writeUTF((String) o);
        else writeUTF(Application.gson.toJson(o));
    }
}
