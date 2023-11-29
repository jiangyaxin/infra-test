package com.jyx.infra.constant;

import com.jyx.infra.util.SystemUtil;

/**
 * @author jiangyaxin
 * @since 2023/11/24 21:04
 */
public interface RuntimeConstant {
    int PROCESSORS = Runtime.getRuntime().availableProcessors();

    String JAVA_IO_TMPDIR = SystemUtil.getJavaIoTmpDir();
}
