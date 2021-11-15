# DBLE监控告警组件

## 实现原理

参考文档: https://actiontech.github.io/dble-docs-cn/1.config_file/1.11_customized_alert.html

通过DBLE的回调事件,实现通知下发

## 使用方式

### 一、下载并安装

​	下载jar包,放到lib目录下, 该jar包无任何三方依赖, 所有依赖项均为DBLE已有依赖 不对DBLE稳定性作出影响

### 配置

1、配置文件配置

```shell
vim $DBLE_HOME/config/dble_alert.properties
```

配置文件详解

```properties
# 固定写法
alert=io.github.luyongwang.dble.WebHookAlarmAlert
# 机器名称
component_id=DBLE-FOR-10.0.142.11
# 企业微信告警 (必须)
web_hook.type=WORK_WECHAT
# 企业微信 全局默认机器人ID (必须)
web_hook.robot_id=xxxx-xxxx-xxxx-xxxx-xxxxxx
# db负责人以及告警配置 dbGroup1 为 db.xml 中的 dbgroup节点name principal是负责人告警中会@ ,多个隔开
web_hook.db_config.dbGroup1.principal=yongwang.lu,chengjian.meng
# robot_id 在一些场景下 想把告警单独分发到某个机器人 可以设置 不设置默认为 全局默认机器人ID
web_hook.db_config.dbGroup1.robot_id=xxxx-yyyy-zzzz-xxxx-yyyy

```

钉钉配置

```properties
# 固定写法
alert=io.github.luyongwang.dble.WebHookAlarmAlert
# 机器名称
component_id=DBLE-FOR-10.0.142.11
# 钉钉告警
web_hook.type=DING_TALK
# 钉钉机器人ID
web_hook.robot_id=xxxxxxxxxxx
web_hook.db_config.dbGroup1.principal=150xxxxxxxx,132xxxxxxxx
web_hook.db_config.dbGroup1.robot_id=xxxxxxxxxxxxxxxxxxxxxxxxxxx
```



自定义WebHook配置

```properties
# 入口
alert=io.github.luyongwang.dble.WebHookAlarmAlert
# DBLE配置
component_id=DBLE-FOR-10.0.142.11
# WEBHOOK配置
web_hook.type=URL
web_hook.hook_url=http://xxxxxxxx/api/v1/robot/msg/send
web_hook.hook_params=robot_id=xxxx-xxxx-xxxx-xxxxxx
web_hook.db_config.dbGroup1.principal=150xxxxxx,132xxxxxx
web_hook.db_config.dbGroup1.hook_params=robot_id=xxx-xxx-xxxx-xxxx-xxxx

```

自定义webHook 请求示例 web hook接口自行实现
``` bash
curl '{hook_url}?{hook_params}' \
   -H 'Content-Type: application/json' \
   -d '
   {
        "msgtype": "markdown",
        "markdown": {
            "content": "<font color='warning'>DBLE告警消息</font>:\n >告警代码:DBLE_HEARTBEAT_FAIL\n >告警解释:心跳后端节点失败\n >告警级别:WARN\n >告警节点:xxx\n >告警时间:2020-10-15 21:48:11\n >告警详情:|dbInstance:dbGroup1-instanceM1|"
        },
        # 手机号方便@等
        "mobile_list": ["150xxxxxx","132xxxxxx"]
   }'
```

