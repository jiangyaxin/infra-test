package com.jyx.infra.mybatis.plus.generator.properties;

import lombok.Data;

/**
 * @author Archforce
 * @since 2023/10/29 15:25
 */
@Data
public class PackageProperties {

    private String parent;

    private String entity;

    private String service;

    private String serviceImpl;

    private String mapper;
}
