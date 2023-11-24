package com.jyx.feature.test.jdk;

import com.jyx.infra.dbf.DbfReader;
import com.jyx.infra.dbf.DbfRecord;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jiangyaxin
 * @since 2023/11/7 10:03
 */
public class DbfTest {

    @Test
    public void readTest() {
        String path = "D:\\document\\自营做市\\data\\202204\\zqbdjs518.DBF";

        List<DbfRecord> dbfRecordList = new ArrayList<>();
         try(DbfReader dbfReader = new DbfReader(new File(path))) {
            DbfRecord dbfRecord = null;
            while ((dbfRecord = dbfReader.read()) !=null) {
                Map<String, Object> data = dbfRecord.toMap();
                dbfRecordList.add(dbfRecord);
            }
             System.out.println(dbfReader.metadata());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(1);
    }
}
