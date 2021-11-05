package com.jyx.infra.jpa.domain.id;

import com.jyx.infra.id.IdAllocator;
import lombok.Getter;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author JYX
 * @since 2021/10/20 16:30
 */
public abstract class AbstractLazyIdGenerator<T extends Serializable> implements IdentifierGenerator {

    @Getter(lazy = true)
    private final IdAllocator<T> generator = initGenerator();

    /**
     * 生成id生成器
     *
     * lazy = true 表示调用 getGenerator() 的时候是双重检查单例模式创建
     *
     * @return IdGenerator
     */
    protected abstract IdAllocator<T> initGenerator();

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return getGenerator().getId();
    }
}
