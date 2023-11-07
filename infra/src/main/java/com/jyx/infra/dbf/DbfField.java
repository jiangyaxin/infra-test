package com.jyx.infra.dbf;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DbfField {

    private int order;

    private String name;

    private DbfFieldTypeEnum type;

    private int length;

    private int scale;

    private int offset;

    @Override
    public String toString() {
        return String.format("DbfField[order=%s,name=%s,type=%s,length=%s,scale=%s,offset=%s]",
                order, name, type, length, scale, offset);
    }
}
