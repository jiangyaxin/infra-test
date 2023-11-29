package com.jyx.infra.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jyx.infra.exception.ExcelException;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/11/28 17:49
 */
public class ExcelUtil {

    public static <T> String writeExcelToPath(String path, List<T> dataList, String sheetName,
                                              Class<?> excelClazz, Collection<String> includeFieldNameCollection) {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            writeExcelToOutputStream(outputStream, dataList, sheetName, excelClazz, includeFieldNameCollection);
            return path;
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

    public static <T> void writeExcelToOutputStream(OutputStream outputStream, List<T> dataList, String sheetName,
                                                    Class<?> excelClazz, Collection<String> includeFieldNameCollection) {
        ExcelWriter writer = null;
        try {
            if (includeFieldNameCollection == null || includeFieldNameCollection.isEmpty()) {
                writer = EasyExcel.write(outputStream, excelClazz).build();
            } else {
                writer = EasyExcel.write(outputStream, excelClazz)
                        .includeColumnFieldNames(new HashSet<>(includeFieldNameCollection))
                        .orderByIncludeColumn(true)
                        .build();
            }
            WriteSheet sheet = EasyExcel.writerSheet(sheetName).build();
            writer.write(dataList, sheet);
        } catch (Exception e) {
            throw new ExcelException(e);
        } finally {
            close(writer);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                throw new ExcelException(e);
            }
        }
    }

}
