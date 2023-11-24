package com.jyx.infra.dbf;

/**
 * @author jiangyaxin
 * @since 2023/11/7 12:30
 */
public interface DbfConstants {

    int HEADER_READ_SIZE = 32;

    int DEFAULT_BUFFER_SIZE = 8192;

    byte DELETED = 0x2A;

    byte HEADER_TERMINATOR = 0x0D;

    byte EMPTY = 0x20;

    byte END_OF_DATA = 0x1A;

    int YEAR_OFFSET = 1900;

    String DEFAULT_CHARSET = "GBK";

    String NUMERIC_OVERFLOW = "*";
    String BOOLEAN_TRUE = "t";
    String BOOLEAN_FALSE = "f";
}
