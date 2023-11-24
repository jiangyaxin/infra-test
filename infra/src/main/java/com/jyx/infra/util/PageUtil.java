package com.jyx.infra.util;

/**
 * @author jiangyaxin
 * @since 2023/11/22 18:38
 */
public class PageUtil {

    public static int calculateNumberOfPage(int totalCount, int pageSize) {
        int remain = totalCount % pageSize;
        int numberOfWholePage = totalCount / pageSize;
        if (remain > 0) {
            return numberOfWholePage + 1;
        }

        return numberOfWholePage;
    }
}
