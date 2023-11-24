package com.jyx.infra.serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author jiangyaxin
 * @since 2023/9/28 14:21
 */
@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class SerializeResult {

    private String filePath;

    private Integer dataCount;

    private Integer fileSize;
}
