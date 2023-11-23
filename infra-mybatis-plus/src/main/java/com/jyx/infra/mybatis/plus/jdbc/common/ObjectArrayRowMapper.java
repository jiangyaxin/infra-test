/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jyx.infra.mybatis.plus.jdbc.common;

import com.jyx.infra.datetime.DateTimeConstant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;

public class ObjectArrayRowMapper implements RowMapper<Object[]> {

    @Override
    public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        Object[] row = new Object[columnCount];
        for (int index = 1; index <= columnCount; index++) {
            Object object = rs.getObject(index);

            row[index - 1] = processSpecialType(object);
        }
        return row;
    }

    private Object processSpecialType(Object object) {
        if (object instanceof Temporal) {
            if (object instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) object;
                return Date.from(localDateTime.toInstant(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE));
            } else if (object instanceof LocalDate) {
                LocalDate localDate = (LocalDate) object;
                return Date.from(localDate.atStartOfDay().toInstant(DateTimeConstant.ZoneOffsets.DEFAULT_ZONE));
            }
        }

        return object;
    }

}
