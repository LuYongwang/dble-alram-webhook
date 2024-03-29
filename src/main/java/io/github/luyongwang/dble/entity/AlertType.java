package io.github.luyongwang.dble.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ("@author yongwang.lu
 */

@AllArgsConstructor
@Getter
public enum AlertType {

    /**
     * 告警Code 和 中文解释
     */
    DBLE_WRITE_TEMP_RESULT_FAIL("DBLE_WRITE_TEMP_RESULT_FAIL", "写出中间结果集到文件失败"),
    DBLE_XA_RECOVER_FAIL("DBLE_XA_RECOVER_FAIL", "XA事务恢复失败"),
    XA_READ_XA_STREAM_FAIL("XA_READ_XA_STREAM_FAIL", "读取XA事务记录文件失败"),
    DBLE_XA_READ_DECODE_FAIL("DBLE_XA_READ_DECODE_FAIL", "解析XA事务记录文件失败"),
    DBLE_XA_READ_IO_FAIL("DBLE_XA_READ_IO_FAIL", "读取XA事务记录文件失败"),
    DBLE_XA_WRITE_IO_FAIL("DBLE_XA_WRITE_IO_FAIL", "写XA事务记录文件失败"),
    DBLE_XA_WRITE_CHECK_POINT_FAIL("DBLE_XA_WRITE_CHECK_POINT_FAIL", "XA写出检查点信息失败"),
    DBLE_XA_BACKGROUND_RETRY_FAIL("DBLE_XA_BACKGROUND_RETRY_FAIL", "XA后台重试提交失败"),
    DBLE_REACH_MAX_CON("DBLE_REACH_MAX_CON", "节点连接数到达配置最大值，获取连接失败"),
    DBLE_TABLE_NOT_CONSISTENT_IN_SHARDINGS("DBLE_TABLE_NOT_CONSISTENT_IN_SHARDINGS", "表结构在不同数据节点中不一致"),
    DBLE_TABLE_NOT_CONSISTENT_IN_MEMORY("DBLE_TABLE_NOT_CONSISTENT_IN_MEMORY", "表结构在数据节点中和dble内存中不一致"),
    DBLE_GLOBAL_TABLE_COLUMN_LOST("DBLE_GLOBAL_TABLE_COLUMN_LOST", "全局表检查列不存在"),
    DBLE_CREATE_CONN_FAIL("DBLE_CREATE_CONN_FAIL", "创建连接到后端mysql的连接失败"),
    DBLE_DB_INSTANCE_CAN_NOT_REACH("DBLE_DB_INSTANCE_CAN_NOT_REACH", "后端连接创建不可达"),
    DBLE_KILL_BACKEND_CONN_FAIL("DBLE_KILL_BACKEND_CONN_FAIL", "Kill后端连接执行失败"),
    DBLE_NIOREACTOR_UNKNOWN_EXCEPTION("DBLE_NIOREACTOR_UNKNOWN_EXCEPTION", "NIO意外报错"),
    DBLE_NIOREACTOR_UNKNOWN_THROWABLE("DBLE_NIOREACTOR_UNKNOWN_THROWABLE", "NIO意外严重错误"),
    DBLE_NIOCONNECTOR_UNKNOWN_EXCEPTION("DBLE_NIOCONNECTOR_UNKNOWN_EXCEPTION", "NIO后端连接创建器意外错误"),
    DBLE_TABLE_LACK("DBLE_TABLE_LACK", "配置中的表格未被创建"),
    DBLE_GET_TABLE_META_FAIL("DBLE_GET_TABLE_META_FAIL", "获取表格的创建语句失败"),
    DBLE_TEST_CONN_FAIL("DBLE_TEST_CONN_FAIL", "测试后端连接可达性失败"),
    DBLE_HEARTBEAT_FAIL("DBLE_HEARTBEAT_FAIL", "心跳后端节点失败"),
    DBLE_SHARDING_NODE_LACK("DBLE_SHARDING_NODE_LACK", "缺少可用的shardingNode节点"),
    DBLE_XA_SUSPECTED_RESIDUE("DBLE_XA_SUSPECTED_RESIDUE", "疑似Xaid残留"),
    DBLE_DB_SLAVE_INSTANCE_DELAY("DBLE_DB_SLAVE_INSTANCE_DELAY", "主从延迟超高delayThreshold的值"),
    DBLE_XA_BACKGROUND_RETRY_STOP("DBLE_XA_BACKGROUND_RETRY_STOP", "XA重试机制中如果配置了xaRetryCount，重试次数到达到该值时，重试停止"),
    SLOW_QUERY_QUEUE_POLICY_ABORT("SLOW_QUERY_QUEUE_POLICY_ABORT", "因队列满了而丢弃慢日志记录"),
    SLOW_QUERY_QUEUE_POLICY_WAIT("SLOW_QUERY_QUEUE_POLICY_WAIT", "慢查询队列满了，但触发了阻塞机制，最终慢日志没有丢失，落盘成功");

    private String code;

    private String msg;

}
