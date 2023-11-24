package com.jyx.infra.serializable.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jyx.infra.datetime.StopWatch;
import com.jyx.infra.serializable.SerializableException;
import com.jyx.infra.serializable.SerializeContext;
import com.jyx.infra.serializable.SerializeResult;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * jdk9+ 启动参数需添加：
 * --add-exports java.base/jdk.internal.ref=ALL-UNNAMED
 * --add-opens java.base/java.nio=ALL-UNNAMED
 *
 * @author jiangyaxin
 * @since 2023/9/28 15:14
 */
@Slf4j
public class KryoNioSerializer extends AbstractKryoSerializer {

    public KryoNioSerializer(SerializeContext serializeContext) {
        super(serializeContext);
    }


    @Override
    public <T> SerializeResult serialize(Collection<T> dataCollection, String filePath) {
        deleteOldFile(filePath);

        Kryo kryo = null;
        MappedByteBuffer mappedByteBuffer = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), Set.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.READ, StandardOpenOption.WRITE));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Output output = new Output(outputStream, serializeContext.getBufferSize())
        ) {
            kryo = kryoPool.obtain();

            ArrayList<T> dataList = wrapToArrayList(dataCollection);
            registerClassToKryo(kryo, dataList);

            kryo.writeObject(output, dataList);
            output.flush();

            int size = outputStream.size();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
            mappedByteBuffer.put(outputStream.toByteArray());
            mappedByteBuffer.force();

            stopWatch.stop();
            long fileSize = fileChannel.size();

            printSerializeSuccessLog(filePath, dataList, stopWatch, fileSize);
            return SerializeResult.of(filePath, dataList.size(), (int) fileSize);
        } catch (Exception e) {
            throw new SerializableException(String.format("Serialize failed: %s", filePath), e);
        } finally {
            closeKryo(kryo);

            closeMappedByteBuffer(mappedByteBuffer);
        }
    }

    @Override
    public <T> List<T> deserialize(Class<T> clazz, String filePath) {
        Kryo kryo = null;
        MappedByteBuffer mappedByteBuffer = null;
        Input input = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try (FileChannel fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ)) {
            long fileSize = fileChannel.size();
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            byte[] bytes = new byte[(int) fileSize];
            mappedByteBuffer.get(bytes);


            kryo = kryoPool.obtain();
            registerClassToKryo(kryo, clazz);
            input = new Input(bytes);
            List<T> result = kryo.readObject(input, ArrayList.class);

            stopWatch.stop();

            printDeserializeSuccessLog(filePath, clazz, stopWatch, fileSize);
            return result;
        } catch (Exception e) {
            throw new SerializableException(String.format("Deserialize failed: %s", filePath), e);
        } finally {
            closeInput(input);

            closeKryo(kryo);

            closeMappedByteBuffer(mappedByteBuffer);
        }
    }
}
