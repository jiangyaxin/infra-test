package com.jyx.infra.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.jyx.infra.serializable.AbstractSerializer;
import com.jyx.infra.serializable.SerializeContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/9/28 14:48
 */
public abstract class AbstractKryoSerializer extends AbstractSerializer {

    protected final KryoPool kryoPool;

    public AbstractKryoSerializer(SerializeContext serializeContext) {
        super(serializeContext);

        this.kryoPool = new KryoPool(true, false, serializeContext.getPoolSize());
    }

    protected void closeKryo(Kryo kryo) {
        if (kryo != null) {
            kryoPool.free(kryo);
        }
    }

    protected void closeInput(Input input) {
        if (input != null) {
            input.close();
        }
    }

    protected <T> ArrayList<T> wrapToArrayList(Collection<T> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return new ArrayList<>();
        }
        if (dataList.getClass() != ArrayList.class) {
            return new ArrayList<>(dataList);
        } else {
            return (ArrayList) dataList;
        }
    }

    protected <T> void registerClassToKryo(Kryo kryo, ArrayList<T> dataList) {
        if (dataList == null || dataList.size() == 0) {
            registerClassToKryo(kryo, Object.class);
        } else {
            Class<?> clazz = dataList.get(0).getClass();
            registerClassToKryo(kryo, clazz);
        }
    }

    protected <T> void registerClassToKryo(Kryo kryo, Class<T> clazz) {
        int registerOffset = 10000;

        CollectionSerializer<List> serializer = new CollectionSerializer<>();
        serializer.setElementClass(clazz);
        kryo.register(clazz, registerOffset++);

        kryo.register(ArrayList.class, serializer, registerOffset++);
        kryo.register(BigDecimal.class, registerOffset++);
        kryo.register(Date.class, registerOffset++);
    }
}
