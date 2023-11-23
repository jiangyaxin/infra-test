package com.jyx.infra.mybatis.plus.query;

/**
 * @author Archforce
 * @since 2023/11/22 18:38
 */
public class PageHelper {

    public static int calculateNumberOfPage(int totalCount, int pageSize) {
        int remain = totalCount % pageSize;
        int numberOfWholePage = totalCount / pageSize;
        if (remain > 0) {
            return numberOfWholePage + 1;
        }

        return numberOfWholePage;
    }
}
