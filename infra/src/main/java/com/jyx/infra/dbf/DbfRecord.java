package com.jyx.infra.dbf;

import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.datetime.DateTimeUtil;
import com.jyx.infra.util.HexUtil;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

public class DbfRecord {

    private byte[] bytes;
    private final DbfMetadata metadata;
    private final Charset charset;
    private final int recordNumber;

    private List<Object> dataList;

    private Map<String, Object> dataMap;

    public DbfRecord(byte[] bytes, DbfMetadata metadata, int recordNumber, Charset charset) {
        this.bytes = bytes;
        this.metadata = metadata;
        this.recordNumber = recordNumber;
        this.charset = charset;
    }

    public List<Object> toList() {
        if (dataList != null) {
            return dataList;
        }

        Map<String, Object> dataMap = toMap();
        List<Object> dataList = new ArrayList<>(dataMap.size());

        List<DbfField> fieldList = metadata.sortedField();
        for (DbfField field : fieldList) {
            String name = field.getName();
            Object object = dataMap.get(name);
            dataList.add(object);
        }

        this.dataList = dataList;
        return dataList;
    }

    public Map<String, Object> toMap() {
        if (dataMap != null) {
            return dataMap;
        }

        Map<String, DbfField> fieldMap = metadata.getFieldMap();
        Map<String, Object> result = new HashMap<>(fieldMap.size() * 2);

        for (Map.Entry<String, DbfField> fieldEntry : fieldMap.entrySet()) {
            String fieldName = fieldEntry.getKey();
            DbfField field = fieldEntry.getValue();
            DbfFieldTypeEnum fieldType = field.getType();
            try {
                switch (fieldType) {
                    case CHARACTER:
                        result.put(fieldName, getString(field));
                        break;
                    case DATE:
                        result.put(fieldName, getDate(field));
                        break;
                    case NUMERIC:
                        result.put(fieldName, getBigDecimal(field));
                        break;
                    case LOGICAL:
                        result.put(fieldName, getBoolean(field));
                        break;
                    case INTEGER:
                        result.put(fieldName, getInteger(field));
                        break;
                    default:
                        throw DbfException.of(String.format("Not support field type: name=%s,type=%s", fieldName, fieldType.name()));
                }
            } catch (Exception e) {
                throw new DbfException(String.format("Parse DbfRecord error,lineNumber=%s,fieldName=%s,type=%s,fieldValue=%s,fieldValueBytes=%s,line=%s",
                        recordNumber, fieldName, fieldType.name(),
                        getString(field), HexUtil.toHexString(getBytes(field)),
                        HexUtil.toHexString(bytes)),
                        e);
            }
        }
        this.dataMap = result;
        this.bytes = null;

        return result;
    }

    public int recordNumber() {
        return recordNumber;
    }

    private String getString(DbfField field) {
        return new String(bytes, field.getOffset(), field.getLength(), charset).trim();
    }

    private Date getDate(DbfField field) {
        String dateStr = getString(field);
        if (dateStr.isEmpty()) {
            return null;
        }
        return DateTimeUtil.parseDate(dateStr, DateTimeConstant.DateTimeFormatters.NO_HORIZONTAL_DATE_FORMATTER);
    }

    private BigDecimal getBigDecimal(DbfField field) {
        String bigDecimalStr = getString(field);

        if (bigDecimalStr.isEmpty()) {
            return null;
        }

        if (bigDecimalStr.contains(DbfConstants.NUMERIC_OVERFLOW)) {
            return null;
        }

        return new BigDecimal(bigDecimalStr);
    }

    private Boolean getBoolean(DbfField field) {
        String booleanStr = getString(field);
        if (booleanStr.isEmpty()) {
            return null;
        }

        if (DbfConstants.BOOLEAN_TRUE.equalsIgnoreCase(booleanStr)) {
            return Boolean.TRUE;
        } else if (DbfConstants.BOOLEAN_FALSE.equalsIgnoreCase(booleanStr)) {
            return Boolean.FALSE;
        } else {
            return null;
        }
    }

    private byte[] getBytes(DbfField field) {
        byte[] b = new byte[field.getLength()];
        System.arraycopy(bytes, field.getOffset(), b, 0, field.getLength());
        return b;
    }

    private Integer getInteger(DbfField field) {
        byte[] bytes = getBytes(field);

        return Bits.makeInt(bytes[0], bytes[1], bytes[2], bytes[3]);
    }
}
