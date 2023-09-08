package com.jyx.infra.web.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.jyx.infra.web.dto.LevelDto;
import com.jyx.infra.web.validation.ValidList;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Archforce
 * @since 2023/9/8 14:38
 */
@Validated
@RestController
@RequestMapping("/logback")
public class LogbackController {

    @PutMapping(value = "/level")
    public List<LevelDto> level(@RequestBody @Validated ValidList<LevelDto> levelDtoList) {
        List<LevelDto> result = new ArrayList<>(levelDtoList.size());

        LoggerContext logbackContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        for(LevelDto levelDto : levelDtoList) {
            String packagePath = levelDto.getPackagePath();
            String level = levelDto.getLevel();

            Logger logger= logbackContext.getLogger(packagePath);
            logger.setLevel(Level.toLevel(level));

            LevelDto levelResult = new LevelDto(logger.getLevel().toString(), packagePath);
            result.add(levelResult);
        }

        return result;
    }
}
