-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: acp_base
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `acp_base`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `acp_base` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `acp_base`;

--
-- Table structure for table `acc_test`
--

DROP TABLE IF EXISTS `acc_test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acc_test` (
                            `secu_id` varchar(50) DEFAULT NULL,
                            `clear_pbu` varchar(50) DEFAULT NULL,
                            `secu_acc_id` varchar(50) DEFAULT NULL,
                            `share_type` varchar(50) DEFAULT NULL,
                            `qty` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fund_secu_acc`
--

DROP TABLE IF EXISTS `fund_secu_acc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fund_secu_acc` (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `branch_id` varchar(20) DEFAULT NULL,
                                 `fund_acc_id` varchar(20) NOT NULL DEFAULT '' COMMENT '资金账号',
                                 `currency` varchar(3) NOT NULL DEFAULT '' COMMENT '币种',
                                 `secu_acc_id` varchar(20) NOT NULL DEFAULT '' COMMENT '证券账户号码',
                                 `market` varchar(2) NOT NULL DEFAULT '' COMMENT '市场代码',
                                 `cust_id` varchar(20) NOT NULL DEFAULT '' COMMENT '客户号',
                                 `secu_acc_type` varchar(2) NOT NULL DEFAULT '' COMMENT '证券账户类别',
                                 `fund_acc_type` varchar(1) NOT NULL DEFAULT '' COMMENT '资金账户类别',
                                 `fee_group_id` varchar(20) NOT NULL DEFAULT '' COMMENT '佣金分组组号',
                                 `trade_sys` varchar(6) NOT NULL DEFAULT '' COMMENT '交易柜台号',
                                 `partition_no` varchar(255) NOT NULL DEFAULT '' COMMENT '分区号/节点号',
                                 `trade_pbu` varchar(6) NOT NULL DEFAULT '' COMMENT '交易单元',
                                 `clear_pbu` varchar(6) NOT NULL DEFAULT '' COMMENT '托管单元',
                                 `secu_acc_status` varchar(2) NOT NULL DEFAULT '' COMMENT '证券账户状态',
                                 `one_code_id` varchar(20) NOT NULL DEFAULT '' COMMENT '一码通账户号码',
                                 `rt_clear_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '实时清算标志',
                                 `entrust_way` varchar(2) NOT NULL DEFAULT '' COMMENT '委托方式',
                                 `fund_check_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否检查资金',
                                 `secu_check_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否检查股份',
                                 `blacklist_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否黑名单客户',
                                 `risky_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否风险客户',
                                 `trade_forbid_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否限制交易客户',
                                 `secu_forbid_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否限售股客户',
                                 `senior_flag` varchar(1) NOT NULL DEFAULT '' COMMENT '是否上市公司董监高客户',
                                 `user_add` bigint DEFAULT '0' COMMENT '录入人员',
                                 `user_check` bigint DEFAULT '0' COMMENT '复核人员',
                                 `auth_biz` varchar(8) NOT NULL DEFAULT '' COMMENT '业务权限',
                                 `auth_acc` varchar(20) NOT NULL DEFAULT '' COMMENT '账户权限',
                                 `comment` varchar(40) NOT NULL DEFAULT '' COMMENT '备注',
                                 `acc_unit` varchar(20) NOT NULL DEFAULT '' COMMENT '持仓单元',
                                 `asset_unit` varchar(25) NOT NULL DEFAULT '' COMMENT '所属资产单元',
                                 `default_acc_unit` varchar(20) NOT NULL DEFAULT '' COMMENT '是否记账单元',
                                 `source_id` bigint NOT NULL DEFAULT '0' COMMENT '数据源id',
                                 `executor` varchar(64) NOT NULL DEFAULT '' COMMENT '执行器名',
                                 `sharding` int NOT NULL COMMENT '分片键',
                                 `time_add` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `time_mod` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_fund_secu_acc` (`branch_id`,`fund_acc_id`,`currency`,`secu_acc_id`,`market`,`acc_unit`,`sharding`),
                                 KEY `idx_source_id` (`source_id`),
                                 KEY `idx_fund_secu_acc_cust_id` (`cust_id`),
                                 KEY `idx_fund_secu_acc_secu_acc_id` (`secu_acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='资金证券账户对应表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Current Database: `jpa_test`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `jpa_test` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `jpa_test`;

--
-- Table structure for table `stage`
--

DROP TABLE IF EXISTS `stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage` (
                         `id` bigint NOT NULL COMMENT '主键',
                         `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                         `created_by` bigint DEFAULT NULL COMMENT '创建人',
                         `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                         `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                         `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage_0`
--

DROP TABLE IF EXISTS `stage_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_0` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage_1`
--

DROP TABLE IF EXISTS `stage_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_1` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage_2`
--

DROP TABLE IF EXISTS `stage_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_2` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_channel`
--

DROP TABLE IF EXISTS `tbl_channel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_channel` (
                               `id` bigint NOT NULL,
                               `created_by` bigint DEFAULT NULL,
                               `created_date` datetime(6) DEFAULT NULL,
                               `last_modified_by` bigint DEFAULT NULL,
                               `last_modified_date` datetime(6) DEFAULT NULL,
                               `version` bigint DEFAULT NULL,
                               `number` int DEFAULT NULL,
                               `light_group_id` bigint DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_dbf`
--

DROP TABLE IF EXISTS `tbl_dbf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_dbf` (
                           `id` bigint NOT NULL,
                           `name` varchar(5) DEFAULT NULL,
                           `qty` decimal(10,4) DEFAULT NULL,
                           `cjsj` datetime DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Current Database: `jpa_test2`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `jpa_test2` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `jpa_test2`;

--
-- Table structure for table `stage_0`
--

DROP TABLE IF EXISTS `stage_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_0` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage_1`
--

DROP TABLE IF EXISTS `stage_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_1` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stage_2`
--

DROP TABLE IF EXISTS `stage_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stage_2` (
                           `id` bigint NOT NULL COMMENT '主键',
                           `name` varchar(8) DEFAULT NULL COMMENT '阶段名称',
                           `created_by` bigint DEFAULT NULL COMMENT '创建人',
                           `created_date` datetime(6) DEFAULT NULL COMMENT '创建日期',
                           `last_modified_by` bigint DEFAULT NULL COMMENT '修改人',
                           `last_modified_date` datetime(6) DEFAULT NULL COMMENT '修改日期',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='阶段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbl_light_group`
--

DROP TABLE IF EXISTS `tbl_light_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tbl_light_group` (
                                   `id` bigint NOT NULL,
                                   `created_by` bigint DEFAULT NULL,
                                   `created_date` datetime(6) DEFAULT NULL,
                                   `last_modified_by` bigint DEFAULT NULL,
                                   `last_modified_date` datetime(6) DEFAULT NULL,
                                   `version` bigint DEFAULT NULL,
                                   `direction` int DEFAULT NULL,
                                   `flow_direction` int DEFAULT NULL,
                                   `number` int DEFAULT NULL,
                                   `type` int DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-04 14:12:23
