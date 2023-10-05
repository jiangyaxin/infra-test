package com.jyx.infra.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.serializable.SerializableException;
import com.jyx.infra.serializable.SerializeContext;
import com.jyx.infra.serializable.SerializeResult;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/9/28 15:14
 */
@Slf4j
public class KryoDefaultSerializer extends AbstractKryoSerializer {

    public KryoDefaultSerializer(SerializeContext serializeContext) {
        super(serializeContext);
    }


    @Override
    public <T> SerializeResult serialize(Collection<T> dataCollection, String filePath) {
        deleteOldFile(filePath);

        Kryo kryo = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        try (FileOutputStream fileOutput = new FileOutputStream(filePath);
             FileChannel fileChannel = fileOutput.getChannel();
             Output output = new Output(fileOutput, serializeContext.getBufferSize())
        ) {
            kryo = kryoPool.obtain();

            ArrayList<T> dataList = wrapToArrayList(dataCollection);
            registerClassToKryo(kryo, dataList);

            kryo.writeObject(output, dataList);
            output.flush();

            stopWatch.stop();
            long fileSize = fileChannel.size();

            printSerializeSuccessLog(filePath, dataList, stopWatch, fileSize);
            return SerializeResult.of(filePath, dataList.size(), (int) fileSize);
        } catch (Exception e) {
            throw new SerializableException(String.format("Serialize failed: %s", filePath), e);
        } finally {
            closeKryo(kryo);
        }
    }

    @Override
    public <T> List<T> deserialize(Class<T> clazz, String filePath) {
        Kryo kryo = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (FileInputStream fileInput = new FileInputStream(filePath);
             FileChannel fileChannel = fileInput.getChannel();
             Input input = new Input(fileInput, serializeContext.getBufferSize())
        ) {
            kryo = kryoPool.obtain();
            registerClassToKryo(kryo, clazz);
            List<T> result = kryo.readObject(input, ArrayList.class);

            stopWatch.stop();
            printDeserializeSuccessLog(filePath, clazz, stopWatch, fileChannel.size());
            return result;
        } catch (Exception e) {
            throw new SerializableException(String.format("Deserialize failed: %s", filePath), e);
        } finally {
            closeKryo(kryo);
        }
    }
}
