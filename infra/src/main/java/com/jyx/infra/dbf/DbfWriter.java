package com.jyx.infra.dbf;

import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.datetime.DateTimeUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

public class DbfWriter implements Closeable {

    private RandomAccessFile randomAccessFile;
    private DbfMetadata metadata;
    private final Charset charset;

    private byte[] oneRecordBuffer;

    private int recordCount = 0;

    public DbfWriter(String path, Supplier<Collection<DbfField>> fieldSupplier) throws IOException {
        this(path, fieldSupplier, DbfConstants.DEFAULT_CHARSET);
    }

    public DbfWriter(String path, Supplier<Collection<DbfField>> fieldSupplier, String charset) throws IOException {
        this.randomAccessFile = new RandomAccessFile(path, "rw");
        this.metadata = new DbfMetadata(fieldSupplier);
        this.charset = charset == null ? Charset.defaultCharset() : Charset.forName(charset);
        this.oneRecordBuffer = new byte[metadata.getOneRecordLength()];

        writeHeader();
        writeFields();
    }

    public synchronized void write(Map<String, Object> map) throws IOException {
        Arrays.fill(oneRecordBuffer, DbfConstants.EMPTY);

        List<DbfField> dbfFieldList = metadata.sortedField();
        for (DbfField field : dbfFieldList) {
            Object o = map.get(field.getName());
            writeIntoRecordBuffer(field, o);
        }
        randomAccessFile.write(oneRecordBuffer);
        recordCount++;
    }


    public void close() throws IOException {
        randomAccessFile.write(DbfConstants.END_OF_DATA);

        randomAccessFile.seek(4);
        byte[] bytes = Bits.makeByte4(recordCount);
        randomAccessFile.write(bytes);

        randomAccessFile.close();

        this.randomAccessFile = null;
        this.oneRecordBuffer = null;
        this.metadata = null;
    }

    private void writeHeader() throws IOException {
        byte[] headerBytes = new byte[DbfConstants.HEADER_READ_SIZE];

        headerBytes[0] = metadata.getFileType().toByte();

        Date updateDate = metadata.getUpdateDate();
        LocalDate updateLocalDate = DateTimeUtil.toLocalDate(updateDate);

        headerBytes[1] = (byte) (updateLocalDate.getYear() - DbfConstants.YEAR_OFFSET);
        headerBytes[2] = (byte) updateLocalDate.getMonthValue();
        headerBytes[3] = (byte) updateLocalDate.getDayOfMonth();

        // 数据条数在close时写入

        byte[] headerLengthBytes = Bits.makeByte2(metadata.getHeaderLength());
        headerBytes[8] = headerLengthBytes[0];
        headerBytes[9] = headerLengthBytes[1];

        byte[] oneRecordBytes = Bits.makeByte2(metadata.getOneRecordLength());
        headerBytes[10] = oneRecordBytes[0];
        headerBytes[11] = oneRecordBytes[1];

        headerBytes[14] = metadata.getUncompletedFlag();
        headerBytes[15] = metadata.getEncryptionFlag();

        randomAccessFile.write(headerBytes);
    }

    private void writeFields() throws IOException {
        List<DbfField> dbfFieldList = metadata.sortedField();

        byte[] bytes = new byte[DbfConstants.HEADER_READ_SIZE];
        for (DbfField field : dbfFieldList) {
            Arrays.fill(bytes, (byte) 0x00);

            String name = field.getName();
            byte[] nameBytes = name.getBytes(charset);
            if (nameBytes.length > 11) {
                throw DbfException.of(String.format("Write dbf error,fieldName more than 11 bytes : %s", name));
            }
            System.arraycopy(nameBytes, 0, bytes, 0, nameBytes.length);

            bytes[11] = field.getType().toByte();

            bytes[16] = (byte) (field.getLength() & 0xFF);
            bytes[17] = (byte) (field.getScale() & 0xFF);

            randomAccessFile.write(bytes);
        }
        randomAccessFile.write(DbfConstants.HEADER_TERMINATOR);
    }

    private void writeIntoRecordBuffer(DbfField dbfField, Object object) {
        if (object == null) {
            return;
        }
        switch (dbfField.getType()) {
            case CHARACTER:
                writeString(dbfField, (String) object);
                break;
            case DATE:
                writeDate(dbfField, (Date) object);
                break;
            case LOGICAL:
                writeBoolean(dbfField, (Boolean) object);
                break;
            case NUMERIC:
                writeBigDecimal(dbfField, (BigDecimal) object);
                break;
            case FLOAT:
                if (object instanceof Double)
                    writeDouble(dbfField, (Double) object);
                else
                    writeFloat(dbfField, (Float) object);
                break;

            case DATETIME:
                writeDateTime(dbfField, (Date) object);
                break;
            case DOUBLE:
            case DOUBLE7:
                writeDouble7(dbfField, (Double) object);
                break;
            case TIMESTAMP:
                writeTimestamp(dbfField, (Date) object);
                break;
            case INTEGER:
                writeInteger(dbfField, (Integer) object);
                break;
            default:
                throw DbfException.of("Unknown or unsupported field type " + dbfField.getType().name() + " for " + dbfField.getName());
        }
    }

    private void writeString(DbfField field, String value) {
        if (value != null) {
            byte[] bytes = value.getBytes(charset);
            if (bytes.length > field.getLength()) {
                throw DbfException.of(String.format("Write dbf error,field more than %s bytes : %s", field.getLength(), field.getName()));
            }
            System.arraycopy(bytes, 0, oneRecordBuffer, field.getOffset(), bytes.length);
        }
    }

    private void writeDate(DbfField field, Date value) {
        if (value != null) {
            String dateStr = DateTimeUtil.formatDate(value, DateTimeConstant.DateTimeFormatters.NO_HORIZONTAL_DATE_FORMATTER);
            byte[] bytes = dateStr.getBytes(charset);
            if (bytes.length > field.getLength()) {
                throw DbfException.of(String.format("Write dbf error,field more than %s bytes : %s", field.getLength(), field.getName()));
            }
            System.arraycopy(bytes, 0, oneRecordBuffer, field.getOffset(), bytes.length);
        }
    }

    private void writeBoolean(DbfField field, Boolean value) {
        if (value != null) {
            String booleanStr = value ? "T" : "F";
            byte[] bytes = booleanStr.getBytes(charset);
            if (bytes.length > field.getLength()) {
                throw DbfException.of(String.format("Write dbf error,field more than %s bytes : %s", field.getLength(), field.getName()));
            }
            System.arraycopy(bytes, 0, oneRecordBuffer, field.getOffset(), bytes.length);
        }
    }

    private void writeBigDecimal(DbfField field, BigDecimal value) {
        if (value != null) {
            String decimalStr = value.toPlainString();
            byte[] bytes = decimalStr.getBytes(charset);
            if (bytes.length > field.getLength()) {
                throw DbfException.of(String.format("Write dbf error,field more than %s bytes : %s", field.getLength(), field.getName()));
            }
            System.arraycopy(bytes, 0, oneRecordBuffer, field.getOffset(), bytes.length);
        }
    }

    private void writeDouble(DbfField field, Double value) {
        if (value != null) {
            String doubleStr = String.format("% 20.18f", value);
            if (doubleStr.length() > 20) {
                doubleStr = doubleStr.substring(0, 20);
            }
            writeString(field, doubleStr);
        }
    }

    private void writeFloat(DbfField f, Float value) {
        writeDouble(f, value.doubleValue());
    }

    private void writeDateTime(DbfField field, Date date) {
        if (date != null) {
            ByteBuffer bb = ByteBuffer.allocate(8);
            bb.putLong(date.getTime());
            System.arraycopy(bb.array(), 0, oneRecordBuffer, field.getOffset(), bb.capacity());
        }
    }

    private void writeDouble7(DbfField field, Double value) {
        if (value != null) {
            ByteBuffer bb = ByteBuffer.allocate(8);
            bb.putDouble(value);
            System.arraycopy(bb.array(), 0, oneRecordBuffer, field.getOffset(), bb.capacity());
        }
    }

    private void writeTimestamp(DbfField field, Date date) {
        if (date != null) {
            LocalDate localDate = DateTimeUtil.toLocalDate(date);
            int year = localDate.getYear();
            int dayOfYear = localDate.getDayOfYear();
            int days = (year - -4713) * 365 + dayOfYear;
            long millis = date.toInstant().getEpochSecond() -
                    localDate.atStartOfDay().toInstant(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE).getEpochSecond();

            ByteBuffer bb = ByteBuffer.allocate(8);

            bb.putInt(0, days);
            bb.putInt(4, (int) millis);

            System.arraycopy(bb.array(), 0, oneRecordBuffer, field.getOffset(), bb.capacity());
        }
    }

    private void writeInteger(DbfField field, Integer value) {
        if (value != null) {
            ByteBuffer bb = ByteBuffer.allocate(4);
            bb.putInt(value);
            System.arraycopy(bb.array(), 0, oneRecordBuffer, field.getOffset(), bb.capacity());
        }
    }
}
