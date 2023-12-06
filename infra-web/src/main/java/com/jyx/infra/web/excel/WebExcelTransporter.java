package com.jyx.infra.web.excel;

import com.google.common.collect.Lists;
import com.google.common.net.MediaType;
import com.jyx.infra.constant.RuntimeConstant;
import com.jyx.infra.datetime.DateTimeConstant;
import com.jyx.infra.datetime.DateTimeUtil;
import com.jyx.infra.exception.ExcelException;
import com.jyx.infra.thread.FutureResult;
import com.jyx.infra.util.ExcelUtil;
import com.jyx.infra.util.FileUtil;
import com.jyx.infra.util.ZipUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author jiangyaxin
 * @since 2023/11/29 11:27
 */
public class WebExcelTransporter {

    interface Constants {

        int MAX_ROWS_IN_EXCEL = 5_0000;

        String XLSX_SUFFIX = ".xlsx";

        String XLSX_ATTACHMENT_TEMPLATE = "attachment;filename=%s" + XLSX_SUFFIX;
        String ZIP_ATTACHMENT_TEMPLATE = "attachment;filename=%s.zip";
    }

    public static <T> void export(HttpServletResponse response, String fileName,
                                  List<T> dataList, Class<T> clazz, Collection<String> includeFieldNameCollection,
                                  ExecutorService executorService) {


        if (CollectionUtils.isEmpty(dataList)) {
            exportExcel(response, fileName, new ArrayList<>(0), clazz, includeFieldNameCollection);
        } else if (dataList.size() < Constants.MAX_ROWS_IN_EXCEL) {
            exportExcel(response, fileName, dataList, clazz, includeFieldNameCollection);
        } else {
            exportZipAtOverLimit(response, fileName, dataList, clazz, includeFieldNameCollection, executorService);
        }
    }

    private static <T> void exportExcel(HttpServletResponse response, String fileName,
                                        List<T> dataList, Class<T> clazz, Collection<String> includeFieldNameCollection) {
        try {
            String sheetName = fileName;
            String charset = StandardCharsets.UTF_8.name();

            response.setContentType(MediaType.OOXML_SHEET.type());
            response.setCharacterEncoding(charset);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(Constants.XLSX_ATTACHMENT_TEMPLATE, UriUtils.encode(fileName, charset)));

            ExcelUtil.writeExcelToOutputStream(response.getOutputStream(), dataList, sheetName, clazz, includeFieldNameCollection);
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }

    private static <T> void exportZipAtOverLimit(HttpServletResponse response, String fileName,
                                                 List<T> dataList, Class<T> clazz, Collection<String> includeFieldNameCollection,
                                                 ExecutorService executorService) {
        String javaIoTmpdir = RuntimeConstant.JAVA_IO_TMPDIR;
        String datetimeStr = DateTimeUtil.formatDateTime(new Date(), DateTimeConstant.DateTimeFormatters.NO_HORIZONTAL_DATE_TIME_FORMATTER);
        File dirFile = new File(javaIoTmpdir);
        if (dirFile.exists()) {
            if (!dirFile.isDirectory()) {
                FileUtil.recursiveDelete(Paths.get(javaIoTmpdir));
                if (!dirFile.mkdir()) {
                    throw new ExcelException("Create tmp dir fail:" + javaIoTmpdir);
                }
            }
        } else {
            if (!dirFile.mkdir()) {
                throw new ExcelException("Create tmp dir fail:" + javaIoTmpdir);
            }
        }

        String sheetName = fileName;
        String charset = StandardCharsets.UTF_8.name();

        List<List<T>> partitionList = Lists.partition(dataList, Constants.MAX_ROWS_IN_EXCEL);
        List<CompletableFuture<String>> futureList = new ArrayList<>(partitionList.size());
        for (int index = 0; index < partitionList.size(); index++) {
            String tmpFileName = fileName + "-" + datetimeStr + "-" + index;
            String filePath = javaIoTmpdir + tmpFileName + Constants.XLSX_SUFFIX;
            List<T> partition = partitionList.get(index);

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> ExcelUtil.writeExcelToPath(filePath, partition, sheetName, clazz, includeFieldNameCollection), executorService);
            futureList.add(future);
        }

        List<String> tmpPathList = FutureResult.mergeCompletableFuture(futureList);

        response.setContentType(MediaType.OCTET_STREAM.type());
        response.setCharacterEncoding(charset);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(Constants.ZIP_ATTACHMENT_TEMPLATE, UriUtils.encode(fileName, charset)));

        try {
            ZipUtil.writeZipToOutputStream(response.getOutputStream(), tmpPathList, true);
        } catch (Exception e) {
            throw new ExcelException(e);
        }
    }
}
