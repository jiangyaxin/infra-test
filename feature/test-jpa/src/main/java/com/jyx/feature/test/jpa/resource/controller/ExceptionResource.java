package com.jyx.feature.test.jpa.resource.controller;

import com.jyx.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

import static com.jyx.infra.web.exception.WebMessageCodes.WRONG_PARAMETER_CODE;

/**
 * @author asa
 * @since 2021/11/7 18:58
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exception")
public class ExceptionResource {

    @PostMapping("/business")
    public String testBusinessException(@RequestBody @NotBlank(message = "参数不能为空") String param){
        throw BusinessException.of(WRONG_PARAMETER_CODE,"Test BusinessException.");
    }
}
