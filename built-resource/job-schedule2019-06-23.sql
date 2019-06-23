/*
SQLyog Ultimate v12.5.0 (64 bit)
MySQL - 5.6.43 : Database - job-scheduling
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`job-scheduling` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `job-scheduling`;


/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `sched_name` VARCHAR(120) NOT NULL,
  `calendar_name` VARCHAR(200) NOT NULL,
  `calendar` BLOB NOT NULL,
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='以 Blob 类型存储 Quartz 的 Calendar 信息';

/*Data for the table `qrtz_calendars` */


/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `entry_id` VARCHAR(95) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `instance_name` VARCHAR(200) NOT NULL,
  `fired_time` BIGINT(13) NOT NULL,
  `sched_time` BIGINT(13) NOT NULL,
  `priority` INT(11) NOT NULL,
  `state` VARCHAR(16) NOT NULL,
  `job_name` VARCHAR(200) DEFAULT NULL,
  `job_group` VARCHAR(200) DEFAULT NULL,
  `is_nonconcurrent` VARCHAR(1) DEFAULT NULL,
  `requests_recovery` VARCHAR(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息';

/*Data for the table `qrtz_fired_triggers` */

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `sched_name` VARCHAR(120) NOT NULL,
  `job_name` VARCHAR(200) NOT NULL,
  `job_group` VARCHAR(200) NOT NULL,
  `description` VARCHAR(250) DEFAULT NULL,
  `job_class_name` VARCHAR(250) NOT NULL,
  `is_durable` VARCHAR(1) NOT NULL,
  `is_nonconcurrent` VARCHAR(1) NOT NULL,
  `is_update_data` VARCHAR(1) NOT NULL,
  `requests_recovery` VARCHAR(1) NOT NULL,
  `job_data` BLOB,
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储每一个已配置的 Job 的详细信息(jobDetail)';

/*Data for the table `qrtz_job_details` */





/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `job_name` VARCHAR(200) NOT NULL,
  `job_group` VARCHAR(200) NOT NULL,
  `description` VARCHAR(250) DEFAULT NULL,
  `next_fire_time` BIGINT(13) DEFAULT NULL,
  `prev_fire_time` BIGINT(13) DEFAULT NULL,
  `priority` INT(11) DEFAULT NULL,
  `trigger_state` VARCHAR(16) NOT NULL,
  `trigger_type` VARCHAR(8) NOT NULL,
  `start_time` BIGINT(13) NOT NULL,
  `end_time` BIGINT(13) DEFAULT NULL,
  `calendar_name` VARCHAR(200) DEFAULT NULL,
  `misfire_instr` SMALLINT(2) DEFAULT NULL,
  `job_data` BLOB,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储已配置的 触发器 (Trigger) 的信息';


/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `cron_expression` VARCHAR(200) NOT NULL,
  `time_zone_id` VARCHAR(80) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储 Cron Trigger，包括 Cron 表达式和时区信息';

/*Data for the table `qrtz_cron_triggers` */

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `blob_data` BLOB,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='以 Blob 类型存储的Trigger';

/*Data for the table `qrtz_blob_triggers` */



/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `sched_name` VARCHAR(120) NOT NULL,
  `lock_name` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储程序的非观锁的信息(假如使用了悲观锁)';

/*Data for the table `qrtz_locks` */

INSERT  INTO `qrtz_locks`(`sched_name`,`lock_name`) VALUES 
('quartzScheduler','TRIGGER_ACCESS');

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储已暂停的 Trigger 组的信息';

/*Data for the table `qrtz_paused_trigger_grps` */

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` VARCHAR(120) NOT NULL,
  `instance_name` VARCHAR(200) NOT NULL,
  `last_checkin_time` BIGINT(13) NOT NULL,
  `checkin_interval` BIGINT(13) NOT NULL,
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储少量的有关调度器 (Scheduler) 的状态，和别的 调度器 (Scheduler)实例(假如是用于一个集群中)';

/*Data for the table `qrtz_scheduler_state` */

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `repeat_count` BIGINT(7) NOT NULL,
  `repeat_interval` BIGINT(12) NOT NULL,
  `times_triggered` BIGINT(10) NOT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='存储简单的 Trigger，包括重复次数，间隔，以及已触的次数';


/*Data for the table `qrtz_simple_triggers` */

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` VARCHAR(120) NOT NULL,
  `trigger_name` VARCHAR(200) NOT NULL,
  `trigger_group` VARCHAR(200) NOT NULL,
  `str_prop_1` VARCHAR(512) DEFAULT NULL,
  `str_prop_2` VARCHAR(512) DEFAULT NULL,
  `str_prop_3` VARCHAR(512) DEFAULT NULL,
  `int_prop_1` INT(11) DEFAULT NULL,
  `int_prop_2` INT(11) DEFAULT NULL,
  `long_prop_1` BIGINT(20) DEFAULT NULL,
  `long_prop_2` BIGINT(20) DEFAULT NULL,
  `dec_prop_1` DECIMAL(13,4) DEFAULT NULL,
  `dec_prop_2` DECIMAL(13,4) DEFAULT NULL,
  `bool_prop_1` VARCHAR(1) DEFAULT NULL,
  `bool_prop_2` VARCHAR(1) DEFAULT NULL,
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_simprop_triggers` */

/*Data for the table `qrtz_triggers` */

/*Table structure for table `tb_app_quartz` */

DROP TABLE IF EXISTS `tb_app_quartz`;

CREATE TABLE `tb_app_quartz` (
  `quartz_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` VARCHAR(100) NOT NULL COMMENT '任务名称',
  `job_group` VARCHAR(100) NOT NULL DEFAULT 'default' COMMENT '任务分组',
  `start_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务开始时间',
  `cron_expression` VARCHAR(20) NOT NULL COMMENT 'corn表格式',
  `invoke_param` VARCHAR(255) NOT NULL COMMENT '需要传递的参数',
  `invoke_param2` VARCHAR(255) DEFAULT NULL COMMENT '需要传递的参数2',
  `provider_name` VARCHAR(255) NOT NULL DEFAULT 'localhost' COMMENT '调用的rpc服务提供方',
  `charge` VARCHAR(255) NOT NULL COMMENT '负责人姓名',
  `charge_department` VARCHAR(255) NOT NULL COMMENT '负责人部门',
  `ctime` TIMESTAMP NOT NULL DEFAULT '1991-01-01 00:00:00' COMMENT '创建时间',
  `mtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `dr` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否有效,标记删除',
  PRIMARY KEY (`quartz_id`),
  UNIQUE KEY `job_name` (`job_name`,`job_group`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='自定义的任务表';

/*Data for the table `tb_app_quartz` */

/*Table structure for table `tb_provider_service_info` */

DROP TABLE IF EXISTS `tb_provider_service_info`;

CREATE TABLE `tb_provider_service_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `provider_name` VARCHAR(255) DEFAULT NULL COMMENT '服务提供者在eruka上注册的应用名',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '服务提供者描述',
  `ip_address` VARCHAR(255) DEFAULT NULL COMMENT '服务的ip地址，选填,可以多个',
  `ctime` TIMESTAMP NOT NULL DEFAULT '1991-01-01 00:00:00' COMMENT '创建时间',
  `mtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `dr` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否有效,标记删除',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='创建服务提供者信息表';

/*Data for the table `tb_provider_service_info` */

/*Table structure for table `tb_quartz_result` */

DROP TABLE IF EXISTS `tb_quartz_result`;

CREATE TABLE `tb_quartz_result` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `quartz_id` INT(11) NOT NULL COMMENT '任务id',
  `dt` CHAR(10) NOT NULL COMMENT '调度日期',
  `start_time` DATETIME NOT NULL COMMENT '调度开始时间',
  `schedule_result` TINYINT(1) NOT NULL COMMENT '调度结果。0,表示失败，1表示成功。2表示执行中',
  `exec_result` TINYINT(1) NOT NULL COMMENT '执行结果。0,表示失败，1表示成功。2表示执行中',
  `exec_time` INT(11) NOT NULL COMMENT '执行时间，毫秒',
  `complete_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '调度/执行完成时间',
  `remark` MEDIUMTEXT COMMENT '备注',
  `ctime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `quartz_id_index` (`quartz_id`),
  KEY `dt_index` (`dt`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='任务执行日志结果表';

/*Data for the table `tb_quartz_result` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
