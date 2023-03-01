package com.jyx.feature.test.redis;

import com.jyx.feature.test.redis.service.CacheTestService;
import com.jyx.infra.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static com.jyx.infra.spring.context.AppConstant.ID_ALLOCATOR;


/**
 * @author JYX
 * @since 2021/11/22 16:14
 */
@Slf4j
@SpringBootTest
@EnableScheduling
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheTestService cacheTestService;

    /**
     * 可以通过 spring.task.execution 配置
     */
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 需要注解@EnableScheduling,并且可以通过 spring.task.scheduling 配置
     */
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Test
    public void testCache() {
        String user = cacheTestService.getUser();
        String post = cacheTestService.getPost();
        user = cacheTestService.getUser();
        post = cacheTestService.getPost();
    }

    @Test
    public void testSet() {

        RedisTestObject testObject = RedisTestObject.builder().id(ID_ALLOCATOR.getId()).message("Test set object.").build();
        redisService.set(testObject.getId().toString(), testObject);

        RedisTestObject getTestObject = (RedisTestObject) redisService.get(testObject.getId().toString());
        Assertions.assertEquals(testObject, getTestObject);

        Boolean isExist = redisService.hasKey(testObject.getId().toString());
        Assertions.assertEquals(true, isExist);

        Boolean isDelete = redisService.delete(testObject.getId().toString());
        Assertions.assertEquals(true, isDelete);

        Object nullObject = redisService.get(testObject.getId().toString());
        Assertions.assertNull(nullObject);

        isDelete = redisService.delete(testObject.getId().toString());
        Assertions.assertEquals(false, isDelete);

        String increaseKey = ID_ALLOCATOR.getId().toString();
        redisService.increase(increaseKey, 2L);

        Long increaseResult = Long.parseLong(redisService.get(increaseKey).toString());
        Assertions.assertEquals(2L, increaseResult);

        redisService.increase(increaseKey, 3L);
        increaseResult = Long.parseLong(redisService.get(increaseKey).toString());
        Assertions.assertEquals(5L, increaseResult);

        redisService.decrease(increaseKey, 3L);
        increaseResult = Long.parseLong(redisService.get(increaseKey).toString());
        Assertions.assertEquals(2L, increaseResult);

        isDelete = redisService.delete(increaseKey);
        Assertions.assertEquals(true, isDelete);
    }

    @Test
    public void testSetTime() throws Exception {

        Long expectExpire = 10L;
        RedisTestObject testObject = RedisTestObject.builder().id(ID_ALLOCATOR.getId()).message("Test set object.").build();
        redisService.set(testObject.getId().toString(), testObject, expectExpire);

        Long expire = redisService.getExpire(testObject.getId().toString());
        Assertions.assertTrue(expire <= expectExpire);

        Thread.sleep(10000);
        Object nullObject = redisService.get(testObject.getId().toString());
        Assertions.assertNull(nullObject);

        redisService.set(testObject.getId().toString(), testObject);
        redisService.expire(testObject.getId().toString(), expectExpire);

        expire = redisService.getExpire(testObject.getId().toString());
        Assertions.assertTrue(expire <= expectExpire);

        Thread.sleep(10000);
        boolean isExist = redisService.hasKey(testObject.getId().toString());
        Assertions.assertFalse(isExist);

    }
}
