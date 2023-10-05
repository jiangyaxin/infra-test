package com.jyx.infra.serializable;

import com.jyx.infra.asserts.Asserts;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Archforce
 * @since 2023/9/28 14:49
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class SerializeContext {

    private Integer poolSize;

    private Integer bufferSize;

    public void validateParameter() {
        Asserts.notNull(poolSize, () -> "Serializer pool size is null.");
        Asserts.notNull(bufferSize, () -> "Serializer buffer size is null.");
    }

}
