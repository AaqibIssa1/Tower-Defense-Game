package edu.coms.sr2.backend.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import edu.coms.sr2.backend.App;

/**
 * Input stream class, providing extra runtime-type-info functionality.
 * @author Nathan
 *
 */

public class BetterInputStream extends DataInputStream {
    public BetterInputStream(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * Reads data as the type given as a parameter. 
     * Any non-primitive/string type is deserialized as UTF8 and further deserialized via Jackson
     * @param <T>
     * @param clazz The intended type of the data to read
     * @return The read value
     * @throws IOException
     */
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
                App.getJsonMapper().readValue((InputStream)this, clazz));
    }
}
