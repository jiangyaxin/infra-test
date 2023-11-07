package com.jyx.infra.dbf;

import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.datetime.DateTimeUtil;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
public class DbfMetadata {

    private DbfFileTypeEnum fileType;

    private Date updateDate;

    private int recordsQty;

    private int headerLength;

    private int oneRecordLength;

    private byte uncompletedFlag;

    private byte encryptionFlag;

    private Map<String, DbfField> fieldMap;

    public DbfMetadata() {
    }

    public DbfMetadata(Supplier<Collection<DbfField>> fieldSupplier) {
        Collection<DbfField> dbfFieldCollection = fieldSupplier.get();

        this.fileType = DbfFileTypeEnum.FOX_BASE_PLUS_1;
        this.updateDate = new Date();
        this.recordsQty = 0;

        this.headerLength = dbfFieldCollection.size() * DbfConstants.HEADER_READ_SIZE + DbfConstants.HEADER_READ_SIZE + 1;
        this.oneRecordLength = dbfFieldCollection.stream()
                .mapToInt(DbfField::getLength)
                .sum();

        this.uncompletedFlag = 0x00;
        this.encryptionFlag = 0x00;

        this.fieldMap = dbfFieldCollection.stream()
                .collect(Collectors.toMap(DbfField::getName, Function.identity()));

    }

    public List<DbfField> sortedField() {
        return fieldMap.values()
                .stream()
                .sorted(Comparator.comparingInt(DbfField::getOrder))
                .collect(Collectors.toList());
    }

    public void parseHead(byte[] headerBytes) {
        // 0字节：DBF版本信息
        this.fileType = DbfFileTypeEnum.fromInt(headerBytes[0]);

        // 1字节：保存时的年 - 1900
        // 2字节：保存时的月
        // 3字节：保存时的日
        this.updateDate = parseHeaderUpdateDate(headerBytes[1], headerBytes[2], headerBytes[3]);

        // 4~7字节：记录数，int32，小端字节序
        this.recordsQty = Bits.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]);

        // 8~9字节：文件头长度，int16，小端字节序
        this.headerLength = Bits.makeInt(headerBytes[8], headerBytes[9]);

        // 10~11字节：每行数据长度，int16，小端字节序
        this.oneRecordLength = Bits.makeInt(headerBytes[10], headerBytes[11]);

        // 14字节：未完成的操作
        this.uncompletedFlag = headerBytes[14];

        // 15字节：dBASE IV编密码标记
        this.encryptionFlag = headerBytes[15];
    }

    public void parseField(byte[] bytes) {
        int offset = 0;
        // 第一个字节为删除标志
        int dataOffset = 1;
        int order = 1;
        Map<String, DbfField> fieldMap = new HashMap<>();
        while (offset < bytes.length && bytes[offset] != DbfConstants.HEADER_TERMINATOR) {
            int oneRecordOffset = 0;

            // 0~10字节：字段名称
            String fieldName = parseFieldName(bytes, offset, oneRecordOffset);
            oneRecordOffset += 11;

            // 11字节：字段数据类型
            DbfFieldTypeEnum fieldType = DbfFieldTypeEnum.fromChar((char) bytes[offset + oneRecordOffset]);
            oneRecordOffset += 1;

            // 12~15字节：保留字节
            oneRecordOffset += 4;

            // 16字节：字段的长度
            int length = bytes[offset + oneRecordOffset];
            if (length < 0) {
                length = 256 + length;
            }
            oneRecordOffset += 1;

            // 17字节：字段的精度
            int scale = bytes[offset + oneRecordOffset];
            oneRecordOffset += 1;

            // 18~19字节：保留字节
            oneRecordOffset += 2;
            // 20字节：工作区ID
            oneRecordOffset += 1;
            // 21~31字节：保留字节
            oneRecordOffset += 11;

            DbfField dbfField = new DbfField(order, fieldName, fieldType, length, scale, dataOffset);
            fieldMap.put(fieldName, dbfField);

            order++;
            dataOffset += length;
            offset += oneRecordOffset;
        }

        this.fieldMap = fieldMap;
    }

    private String parseFieldName(byte[] bytes, int offset, int oneRecordOffset) {
        int nameIndex = 0;
        for (; nameIndex < 11 && bytes[nameIndex] > 0; nameIndex++) {

        }
        return new String(bytes, offset + oneRecordOffset, nameIndex);
    }


    private Date parseHeaderUpdateDate(byte yearByte, byte monthByte, byte dayByte) {
        int year = yearByte + DbfConstants.YEAR_OFFSET;
        int month = monthByte;
        int day = dayByte;
        return Date.from(LocalDate.of(year, month, day).atStartOfDay().toInstant(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE));
    }


    @Override
    public String toString() {
        String updateDateStr = DateTimeUtil.formatDate(updateDate, DateTimeConstant.DateTimeFormatters.DEFAULT_DATE_FORMATTER);

        return String.format("DbfMetadata [type=%s,updateDate=%s,recordsQty=%s,headerLength=%s,oneRecordLength=%s,fields=%s]",
                fileType, updateDateStr, recordsQty, headerLength, oneRecordLength,
                sortedField().stream()
                        .map(DbfField::toString)
                        .collect(Collectors.joining(",\n", "\n", "")));
    }
}
