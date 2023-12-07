create table sharding_table_rule
(
    id                               bigint       not null comment '主键' primary key,
    logic_table                      varchar(32)  not null comment '逻辑表名',
    actual_data_nodes                varchar(128) not null comment '物理表名，逗号分割',
    database_sharding_strategy_type  varchar(32) null comment '分库策略类型',
    database_sharding_strategy_param varchar(128) null comment '分库策略参数，JSON字符串',
    table_sharding_strategy_type     varchar(32) null comment '分表策略类型',
    table_sharding_strategy_param    varchar(128) null comment '分表策略参数，JSON字符串',
    key_generate_strategy_param      varchar(128) null comment '主键生成策略参数，JSON字符串',
    audit_strategy_param             varchar(128) null comment '审计策略参数，JSON字符串',
    created_date                     datetime null comment '创建时间',
    last_modified_date               datetime null comment '修改时间',
    constraint uidx_logic_table
    unique (logic_table)
) comment '分片路由配置表';