

import com.jyx.infra.id.CachedSnowflakeIdAllocator;
import com.jyx.infra.id.SnowflakeIdFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jyx.infra.datetime.DateTimeConstant.DATETIME_FORMATTER;

/**
 * @author JYX
 * @since 2021/10/20 15:55
 */
@Slf4j
public class CachedSnowflakeIdGeneratorTest {
    private static final int SIZE = 7000000; // 700w
    private static final boolean VERBOSE = false;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

    CachedSnowflakeIdAllocator defaultSnowflakeIdGenerator = new CachedSnowflakeIdAllocator(SnowflakeIdFormatter.DEFAULT_FORMATTER,1,1);

    @Test
    public void testSerialGenerate() {
        log.info("testSerialGenerate--------------------"+ LocalDateTime.now().format(DATETIME_FORMATTER));
        Set<Long> idSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(idSet,i);
        }

        checkUniqueID(idSet);
    }

    @Test
    public void testParallelGenerate() throws InterruptedException {
        log.info("testParallelGenerate--------------------"+LocalDateTime.now().format(DATETIME_FORMATTER));
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
        long id = defaultSnowflakeIdGenerator.getId();
        String parsedInfo = defaultSnowflakeIdGenerator.parseId(id);
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
