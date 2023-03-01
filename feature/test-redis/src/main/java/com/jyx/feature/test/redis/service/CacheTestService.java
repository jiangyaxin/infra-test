package com.jyx.feature.test.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author JYX
 * @since 2021/11/23 9:45
 */
@Slf4j
@Service
public class CacheTestService {

    /**
     * methodName    |	root对象  |	当前被调用的方法名          |	#root.methodname
     * method	     |  root对象  |	当前被调用的方法            |	#root.method.name
     * target        |	root对象  |	当前被调用的目标对象实例    |	#root.target
     * targetClass   |	root对象  |	当前被调用的目标对象的类	   |    #root.targetClass
     * args        	 |  root对象  |	当前被调用的方法的参数列表   |    #root.args[0]
     * caches	     |  root对象  | 当前方法调用使用的缓存列表   |   #root.caches[0].name
     * Argument Name |	执行上下文 |	当前被调用的方法的参数，如findArtisan(Artisan artisan),可以通过#artsian.id获得参数            |	#artsian.id
     * result	     |  执行上下文 |	方法执行后的返回值（仅当方法执行后的判断有效，如 unless cacheEvict的beforeInvocation=false）   |	#result
     */
    @Cacheable(value = "user", key = "#root.args")
    public String getUser() {
        log.info("Generate user.");
        return "user";
    }

    @Cacheable(value = "post", key = "#root.args")
    public String getPost() {
        log.info("Generate post.");
        return "post";
    }

    @Cacheable(value = "user1", key = "#root.args")
    public String getUser1() {
        log.info("Generate user1.");
        return "user1";
    }
}
