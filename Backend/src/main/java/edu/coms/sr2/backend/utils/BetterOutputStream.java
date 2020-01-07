package edu.coms.sr2.backend.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import edu.coms.sr2.backend.App;

/**
 * Input stream class providing extra functionality for writing objects based on runtime-type
 * @author Nathan
 *
 */

public class BetterOutputStream extends DataOutputStream {
    public BetterOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    /**
     * Writes any object, either using appropriate methods for primitives/strings, or as a JSON string from Jackson
     * @param o The object to write
     * @return This object, for chaining
     * @throws IOException
     */
    public BetterOutputStream write(Object o) throws IOException {
    	if(o instanceof byte[]) write((byte[]) o);
    	else if(o instanceof Integer) writeInt((Integer) o);
        else if(o instanceof Short) writeShort((Short) o);
        else if(o instanceof Long) writeLong((Long) o);
        else if(o instanceof Float) writeFloat((Float) o);
        else if(o instanceof Double) writeDouble((Double) o);
        else if(o instanceof Boolean) writeBoolean((Boolean) o);
        else if(o instanceof String) writeUTF((String) o);
        else writeUTF(App.getJsonMapper().writeValueAsString(o));
    	
    	return this;
    }
}
