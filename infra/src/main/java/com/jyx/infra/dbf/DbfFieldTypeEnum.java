package com.jyx.infra.dbf;

import lombok.Getter;

@Getter
public enum DbfFieldTypeEnum {

    CHARACTER('C'),

    CURRENCY('Y'),

    NUMERIC('N'),

    FLOAT('F'),

    DATE('D'),

    /**
     * @deprecated FoxPro-specific extension. Use Timestamp/@ with dBASE 7 or later
     */
    @Deprecated
    DATETIME('T'),

    TIMESTAMP('@'),

    /**
     * @deprecated Binary doubles are FoxPro specific dBASE V uses B for binary MEMOs. Use Double7, Float or Numeric instead
     */
    @Deprecated
    DOUBLE('B'),

    /**
     * dBASE 7 binary double (standardized in contrast to 'B'
     */
    DOUBLE7('O'),

    INTEGER('I'),

    LOGICAL('L'),

    MEMO('M'),

    GENERAL('G'),

    PICTURE('P'),

    NULL_FLAGS('0');

    final char type;

    DbfFieldTypeEnum(char type) {
        this.type = type;
    }

    public static DbfFieldTypeEnum fromChar(char type) {
        for (DbfFieldTypeEnum e : DbfFieldTypeEnum.values()) {
            if (e.type == type) {
                return e;
            }
        }
        throw DbfException.of(String.format("Unknown dbf field type:%s", type));
    }

    public byte toByte() {
        return (byte) type;
    }

}
