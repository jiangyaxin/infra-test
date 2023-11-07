package com.jyx.infra.dbf;


import com.jyx.infra.log.Logs;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
public class DbfMerger {

    public static String merger(String targetPath, String... sourcePaths) {
        if (sourcePaths == null || sourcePaths.length == 0) {
            throw DbfException.of("Merge dbf error,path is null");
        }
        List<String> sourcePathList = Arrays.asList(sourcePaths);

        try {
            Path path = FileSystems.getDefault().getPath(targetPath);
            Files.deleteIfExists(path);

            int headSize = -1;
            int count = 0;

            final ByteBuffer headBuffer = ByteBuffer.allocate(DbfConstants.HEADER_READ_SIZE);
            try (FileChannel fileChannel = FileChannel.open(path, EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {
                for (final String source : sourcePathList) {
                    try (FileChannel sourceChannel = FileChannel.open(FileSystems.getDefault().getPath(source), EnumSet.of(StandardOpenOption.READ))) {
                        headBuffer.clear();
                        sourceChannel.read(headBuffer);
                        final byte[] headBytes = headBuffer.array();
                        final int sourceHeadSize = Bits.makeInt(headBytes[8], headBytes[9]);
                        final int sourceRowSize = Bits.makeInt(headBytes[4], headBytes[5], headBytes[6], headBytes[7]);
                        if (sourceRowSize < 0) {
                            Logs.warn(log, "{} is empty,0 lines", source);
                            continue;
                        }

                        if (headSize < 0) {
                            headSize = sourceHeadSize;
                            sourceChannel.transferTo(0L, headSize, fileChannel);
                        } else if (headSize != sourceHeadSize) {
                            throw DbfException.of(String.format("%s head bytes length error", source));
                        }
                        sourceChannel.transferTo(headSize, sourceChannel.size() - 1L, fileChannel);
                        count += sourceRowSize;
                    }
                }
                ByteBuffer endFLag = ByteBuffer.allocate(1).put(DbfConstants.END_OF_DATA);
                endFLag.flip();
                fileChannel.write(endFLag);
                ByteBuffer sizeBuffer = ByteBuffer.allocate(4).putInt(Bits.littleEndian(count));
                sizeBuffer.flip();
                fileChannel.write(sizeBuffer, 4L);
            }

            Logs.info(log, "Merge dbf success,result save at {},source is {}", targetPath, sourcePathList);
            return targetPath;
        } catch (Exception e) {
            throw DbfException.of(String.format("Merge dbf failed,source is %s", sourcePathList), e);
        }
    }
}
