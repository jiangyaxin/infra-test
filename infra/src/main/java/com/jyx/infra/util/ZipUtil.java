package com.jyx.infra.util;

import com.jyx.infra.exception.ZipException;
import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Archforce
 * @since 2023/11/29 15:59
 */
@Slf4j
public class ZipUtil {

    interface Constants {
        int BUFFER_SIZE = 4096;
    }

    public static void writeZipToOutputStream(OutputStream outputStream, Collection<String> zipChildrenFileCollection, boolean deleteSourceFile) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (String zipChildrenFilePath : zipChildrenFileCollection) {
                File zipChildrenFile = new File(zipChildrenFilePath);
                try (FileInputStream fileInputStream = new FileInputStream(zipChildrenFile)) {
                    zipOutputStream.putNextEntry(new ZipEntry(zipChildrenFile.getName()));
                    byte[] buffer = new byte[Constants.BUFFER_SIZE];
                    int len;
                    while ((len = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    throw e;
                }
                if (deleteSourceFile) {
                    if (!zipChildrenFile.delete()) {
                        Logs.debug(log, "Deleted zip source file: {}", zipChildrenFilePath);
                    }
                }
            }
            zipOutputStream.finish();
        } catch (Exception e) {
            throw new ZipException(e);
        }
    }
}
