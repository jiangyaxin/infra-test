package com.jyx.feature.test.mybatis.plus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author jiangyaxin
 * @since 2021/10/13 16:00
 */
@SpringBootTest(classes = {MybatisPlusApplication.class})
public class MybatisPlusTestApplication {

    @Autowired
    private FastAutoGenerator fastAutoGenerator;

    @Test
    public void genTest() {
        fastAutoGenerator.execute();
    }

}

