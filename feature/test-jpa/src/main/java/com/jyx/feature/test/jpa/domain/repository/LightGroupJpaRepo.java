package com.jyx.feature.test.jpa.domain.repository;

import com.jyx.feature.test.jpa.domain.entity.LightGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 注解@EntityGraph 本质上就是 left outer join 语句,注意查询结果
 *
 * @author JYX
 * @since 2021/10/20 17:50
 */
@Repository
public interface LightGroupJpaRepo extends JpaRepository<LightGroup,Long> {

    /**
     * left-join
     * 添加 @EntityGraph("fetchChannelList") 会导致一对多情况下，light_group中查询的channel编号为2，查询不全
     * 不添加 @EntityGraph("fetchChannelList") 会先查询channel编号为2的light_group,再根据light_group_id 查询到所有的channel
     *
     * @param channelNumber 通道编号
     * @return 通道编号为channelNumber的灯组
     */
//    @EntityGraph("fetchChannelList")
    List<LightGroup> findByChannelListNumber(Integer channelNumber);

    /**
     * 如果添加 @EntityGraph("fetchChannelList") 注解，删除 通道编号等于2的灯组，级联删除left-join编号等于2的通道，只删除查到的数据,未能删除属于该灯组但是通道编号不为2的通道
     * 如果不添加 @EntityGraph("fetchChannelList") 使用的 n+1 查询语句，先查询light_group_id,再用light_group_id查询channel，能删除完整
     *
     * 注解@Modifying作用：
     *
     * （1）可以通过自定义的 JPQL 完成 UPDATE 和 DELETE 操作。 注意： JPQL 不支持使用 INSERT；
     * （2）在 @Query 注解中编写 JPQL 语句， 但必须使用 @Modifying 进行修饰. 以通知 SpringData， 这是一个 UPDATE 或 DELETE 操作
     * （3）UPDATE 或 DELETE 操作需要使用事务，此时需要定义 Service 层，在 Service 层的方法上添加事务操作；
     * （4）默认情况下， SpringData 的每个方法上有事务， 但都是一个只读事务。 他们不能完成修改操作。
     *
     * @param channelNumber 通道编号
     * @return 返回删除的数目
     */
//    @EntityGraph("fetchChannelList")
    @Transactional(rollbackFor = Exception.class)
    int deleteByChannelListNumber(Integer channelNumber);
}
