package com.jyx.infra.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;

/**
 * @author Archforce
 * @since 2023/9/28 14:46
 */
public class KryoPool extends Pool<Kryo> {

    public KryoPool(boolean threadSafe, boolean softReferences) {
        super(threadSafe, softReferences);
    }

    public KryoPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
    }

    @Override
    protected Kryo create() {
        return new Kryo();
    }
}
