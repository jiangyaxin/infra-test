package com.jyx.infra.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Archforce
 * @since 2023/11/6 10:57
 */
@AllArgsConstructor
@Getter
public class CheckResult {

    private boolean success;

    private String message;

    public static CheckResult success(String message) {
        return new CheckResult(true, message);
    }

    public static CheckResult fail(String message) {
        return new CheckResult(false, message);
    }
}
