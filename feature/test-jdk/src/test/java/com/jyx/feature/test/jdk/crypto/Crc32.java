package com.jyx.feature.test.jdk.crypto;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

@Slf4j
public final class Crc32 {

    private Crc32() {
    }

    /**
     * 计算二进制字节校验码
     *
     * @param data   二进制数据
     * @param offset 起始字节索引
     * @param length 校验字节长度
     * @return 校验码
     */
    public static long bytes(byte[] data, int offset, int length) {
        CRC32 crc32 = new CRC32();
        crc32.update(data, offset, length);
        return crc32.getValue();
    }

    /**
     * 计算二进制字节校验码
     *
     * @param data 二进制数据
     * @return 校验码
     */
    public static long bytes(byte[] data) {
        return bytes(data, 0, data.length);
    }

    /**
     * 对文件内容计算crc32校验码
     *
     * @param file 需要计算crc32校验码的文件
     * @return crc校验码
     * @throws IOException 读取文件异常
     */
    public static long file(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buff = new byte[64 * 1024];
        int len;
        CRC32 crc32 = new CRC32();
        try {
            while ((len = fileInputStream.read(buff)) != -1) {
                crc32.update(buff, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            fileInputStream.close();
        }

        return crc32.getValue();
    }
}