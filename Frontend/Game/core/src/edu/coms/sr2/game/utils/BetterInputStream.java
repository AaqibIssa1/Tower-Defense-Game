package edu.coms.sr2.game.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.coms.sr2.game.Application;

public class BetterInputStream extends DataInputStream {

    public BetterInputStream(InputStream inputStream) {
        super(inputStream);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(Class<T> clazz) throws IOException {
        return (T)(
                clazz == int.class ? readInt() :
                clazz == short.class ? readShort() :
                clazz == long.class ? readLong() :
                clazz == float.class ? readFloat() :
                clazz == double.class ? readDouble() :
                clazz == boolean.class ? readBoolean() :
                clazz == String.class ? readUTF() :
                Application.gson.fromJson(readUTF(), clazz));
    }
}
