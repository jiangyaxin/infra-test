package com.jyx.feature.test.disruptor;

import com.jyx.infra.thread.NamingThreadFactory;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

/**
 * @author jiangyaxin
 * @since 2022/12/1 9:55
 */
@Slf4j
public class DisruptorTests {

    @Test
    public void disruptorTest() throws Exception {
        EventFactory<Truck<Integer>> truckFactory = Truck::new;
        ThreadFactory threadFactory = new NamingThreadFactory("disruptor");
        WaitStrategy waitStrategy = new YieldingWaitStrategy();
        int ringSize = 2 << 10;
        Disruptor<Truck<Integer>> disruptor = new Disruptor<>(truckFactory, ringSize, threadFactory, ProducerType.MULTI, waitStrategy);

        WorkHandler<Truck<Integer>> consumer1 = truck -> log.info("consumer1 ------ {}", truck.getData());
        WorkHandler<Truck<Integer>> consumer2 = truck -> log.info("consumer2 ------ {}", truck.getData());
        WorkHandler<Truck<Integer>> consumer3 = truck -> log.info("consumer3 ------ {}", truck.getData());
        EventHandler<Truck<Integer>> consumer4 = (truck, sequence, endOfBatch) -> log.info("consumer4 ------ {}", truck.getData());
        disruptor.handleEventsWithWorkerPool(consumer1, consumer2, consumer3).then(consumer4);
        disruptor.start();

        for (int i = 0; i < 20; i++) {
            Integer data = i;
            disruptor.publishEvent((truck, sequence) -> truck.load(data));
        }

        Thread.currentThread().join();
    }


    @Test
    public void disruptorOutOfMemoryTest() throws Exception {
        EventFactory<Truck<byte[]>> truckFactory = Truck::new;
        ThreadFactory threadFactory = new NamingThreadFactory("disruptor");
        WaitStrategy waitStrategy = new YieldingWaitStrategy();
        int ringSize = 2 << 10;
        Disruptor<Truck<byte[]>> disruptor = new Disruptor<>(truckFactory, ringSize, threadFactory, ProducerType.MULTI, waitStrategy);

        WorkHandler<Truck<byte[]>> consumer1 = truck -> {
            log.info("consumer1 ------ {}", "消费数据");
            truck.clear();
        };
        disruptor.handleEventsWithWorkerPool(consumer1);
        disruptor.start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                byte[] data = new byte[1024*1024];
                disruptor.publishEvent((truck, sequence) -> truck.load(data));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        Thread.currentThread().join();
    }
}
