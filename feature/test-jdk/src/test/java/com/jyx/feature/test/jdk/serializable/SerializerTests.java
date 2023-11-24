package com.jyx.feature.test.jdk.serializable;

import com.jyx.feature.test.jdk.Flow;
import com.jyx.infra.serializable.SerializeContext;
import com.jyx.infra.serializable.SerializeResult;
import com.jyx.infra.serializable.kryo.KryoNioSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2023/10/4 16:56
 */
@Slf4j
public class SerializerTests {

    private static final String FILE_PATH = "D:\\1\\test.bin";

    private static final Integer COUNT = 10_0000;

    @Test
    public void kryoNioSerializerTest() {
        SerializeContext serializeContext = SerializeContext.of(1000, 1024);
        KryoNioSerializer kryoNioSerializer = new KryoNioSerializer(serializeContext);

        nullSerializeTest(kryoNioSerializer);
        emptySerializeTest(kryoNioSerializer);

        multiSerializeTest(kryoNioSerializer);
    }

    private static void multiSerializeTest(KryoNioSerializer kryoNioSerializer) {
        List<Flow> sourceList = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            String iStr = String.valueOf(i);
            Flow flow = new Flow(iStr, iStr);
            sourceList.add(flow);
        }
        SerializeResult serializeResult = kryoNioSerializer.serialize(sourceList, FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = kryoNioSerializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList.size());
    }

    private static void nullSerializeTest(KryoNioSerializer kryoNioSerializer) {
        SerializeResult serializeResult = kryoNioSerializer.serialize(new ArrayList<>(), FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = kryoNioSerializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList);
    }

    private static void emptySerializeTest(KryoNioSerializer kryoNioSerializer) {
        SerializeResult serializeResult = kryoNioSerializer.serialize(new ArrayList<>(), FILE_PATH);
        log.info(serializeResult.toString());

        List<Flow> flowList = kryoNioSerializer.deserialize(Flow.class, FILE_PATH);
        log.info("{}", flowList);
    }
}
