package com.jyx.infra.dbf;

import java.io.*;
import java.nio.charset.Charset;

public class DbfReader implements Closeable {

    private InputStream dbfInputStream;

    private DbfMetadata metadata;

    private int recordsCounter = 1;

    private final Charset charset;

    public DbfReader(File dbfFile) throws IOException {
        this(new FileInputStream(dbfFile), DbfConstants.DEFAULT_BUFFER_SIZE, DbfConstants.DEFAULT_CHARSET);
    }

    public DbfReader(File dbfFile, int bufferSize, String charset) throws IOException {
        this(new FileInputStream(dbfFile), bufferSize, charset);
    }

    public DbfReader(InputStream dbfInputStream, int bufferSize, String charset) throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, bufferSize);
        this.metadata = readMetadata();
        this.charset = charset == null ? Charset.defaultCharset() : Charset.forName(charset);
    }

    public DbfRecord read() throws IOException {
        int oneRecordLength = metadata.getOneRecordLength();
        byte[] bytes = new byte[oneRecordLength];
        do {
            if (dbfInputStream.read(bytes) != oneRecordLength) {
                if (recordsCounter - 1 != metadata.getRecordsQty()) {
                    throw new IOException("The file is corrupted or is not a dbf file, read data error.");
                }
                return null;
            }
        } while (bytes[0] == DbfConstants.DELETED);

        return new DbfRecord(bytes, metadata, recordsCounter++, charset);
    }

    private DbfMetadata readMetadata() throws IOException {
        byte[] headerBytes = new byte[DbfConstants.HEADER_READ_SIZE];
        if (dbfInputStream.read(headerBytes) != DbfConstants.HEADER_READ_SIZE) {
            throw new IOException("The file is corrupted or is not a dbf file, read header bytes error.");
        }

        DbfMetadata dbfMetadata = new DbfMetadata();
        dbfMetadata.parseHead(headerBytes);

        int fieldSize = dbfMetadata.getHeaderLength() - headerBytes.length;
        byte[] fieldBytes = new byte[fieldSize];
        if (dbfInputStream.read(fieldBytes) != fieldSize) {
            throw new IOException("The file is corrupted or is not a dbf file, read field bytes error.");
        }

        dbfMetadata.parseField(fieldBytes);

        return dbfMetadata;
    }

    public DbfMetadata metadata() {
        return metadata;
    }

    @Override
    public void close() throws IOException {
        if (dbfInputStream != null) {
            dbfInputStream.close();
            dbfInputStream = null;
        }
        metadata = null;
        recordsCounter = 0;
    }
}
