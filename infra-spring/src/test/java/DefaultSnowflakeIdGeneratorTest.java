
import com.jyx.infra.id.DefaultSnowflakeIdAllocator;
import com.jyx.infra.id.SnowflakeIdFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jyx.infra.datetime.DateTimeConstant.DateTimeFormatters.DEFAULT_DATETIME_FORMATTER;


/**
 * @author jiangyaxin
 * @since 2021/10/20 1:53
 */
@Slf4j
public class DefaultSnowflakeIdGeneratorTest {

    // 10W
    private static final int SIZE = 100000;
    private static final boolean VERBOSE = true;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

    DefaultSnowflakeIdAllocator defaultSnowflakeIdAllocator = new DefaultSnowflakeIdAllocator(SnowflakeIdFormatter.DEFAULT_FORMATTER,1,1);

    @Test
    public void testSerialGenerate() {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("testSerialGenerate--------------------"+LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER));
        Set<Long> idSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(idSet,i);
        }
        watch.stop();
        log.info("DefaultSnowflakeIdGeneratorTest.testSerialGenerate 耗时：{}ms",watch.getTotalTimeMillis());

        checkUniqueID(idSet);
    }

    @Test
    public void testParallelGenerate() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        log.info("testParallelGenerate--------------------"+LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER));
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();

        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRun(uidSet, control));
            thread.setName("UID-generator-" + i);

            threadList.add(thread);
            thread.start();
        }

        // 等待任务完成
        for (Thread thread : threadList) {
            thread.join();
        }
        watch.stop();
        log.info("DefaultSnowflakeIdGeneratorTest.testParallelGenerate 耗时：{}ms",watch.getTotalTimeMillis());

        Assert.isTrue(SIZE == control.get(),"thread not use the same control id.");

        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (;;) {
            int myPosition = control.updateAndGet(old -> (old == SIZE ? SIZE : old + 1));
            if (myPosition == SIZE) {
                return;
            }

            doGenerate(uidSet, myPosition);
        }
    }

    private void doGenerate(Set<Long> uidSet,int index) {
        long id = defaultSnowflakeIdAllocator.getId();
        String parsedInfo = defaultSnowflakeIdAllocator.parseId(id);
        uidSet.add(id);

        Assert.isTrue(id > 0L,"id <= 0.");
        Assert.hasText(parsedInfo,"id is null.");

        if (VERBOSE) {
            log.info(Thread.currentThread().getName() + " No." + index + " >>> " + parsedInfo);
        }
    }

    private void checkUniqueID(Set<Long> idSet) {
        log.info("id size is {}",idSet.size());
        Assert.isTrue(SIZE == idSet.size(),"id duplicate.");
    }

}
