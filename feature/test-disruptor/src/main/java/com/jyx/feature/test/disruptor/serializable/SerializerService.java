package com.jyx.feature.test.disruptor.serializable;

import com.jyx.feature.test.disruptor.domain.entity.Flow;
import com.jyx.infra.serializable.SerializeContext;
import com.jyx.infra.serializable.SerializeResult;
import com.jyx.infra.serializable.Serializer;
import com.jyx.infra.serializable.kryo.KryoDefaultSerializer;
import com.jyx.infra.serializable.kryo.KryoNioSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/10/4 16:56
 */
@Slf4j
@Service
public class SerializerService {

    private static final String FILE_PATH = "D:\\1\\test.bin";

    private static final Integer COUNT = 10_0000;


    public void kryoNioSerializerTest() {
        SerializeContext serializeContext = SerializeContext.of(1000, 1024);
        KryoNioSerializer kryoNioSerializer = new KryoNioSerializer(serializeContext);
        KryoDefaultSerializer kryoDefaultSerializer = new KryoDefaultSerializer(serializeContext);

        nullSerializeTest(kryoDefaultSerializer);
        emptySerializeTest(kryoDefaultSerializer);

//        multiSerializeTest(kryoNioSerializer);
        multiSerializeTest(kryoDefaultSerializer);
    }

    private void multiSerializeTest(Serializer serializer) {
        List<Flow> sourceList = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            String iStr = String.valueOf(i);
            Flow flow = new Flow(iStr, iStr);
            sourceList.add(flow);
        }
        SerializeResult serializeResult = serializer.serialize(sourceList, FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = serializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList.size());
    }

    private static void nullSerializeTest(Serializer serializer) {
        SerializeResult serializeResult = serializer.serialize(new ArrayList<>(), FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = serializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList);
    }

    private static void emptySerializeTest(Serializer serializer) {
        SerializeResult serializeResult = serializer.serialize(new ArrayList<>(), FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = serializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList);
    }
}
