package io.github.luyongwang.dble.config;


import com.actiontech.dble.alarm.AlertGeneralConfig;
import com.actiontech.dble.config.model.SystemConfig;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.luyongwang.dble.entity.WebHookAlarmConfig;
import io.github.luyongwang.dble.util.JsonUtil;
import io.github.luyongwang.dble.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 告警数据解析器，转为JavaBean方便使用
 *
 * @author yongwang.lu
 */
@Slf4j
public class AlarmConfiguration {

    public static WebHookAlarmConfig getWebHookAlarmConfig() {

        Properties properties = AlertGeneralConfig.getInstance().getProperties();

        // DBLE实例配置
        SystemConfig instanceConfig = SystemConfig.getInstance();
        JsonObject jsonObject = PropertiesUtil.convertToJson(properties);

        Gson gson = new GsonBuilder()
                // 对value为null的属性也进行序列化
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        WebHookAlarmConfig webHookAlarmConfig = gson.fromJson(JsonUtil.toJson(jsonObject), WebHookAlarmConfig.class);
        webHookAlarmConfig.setBindIp(instanceConfig.getBindIp());
        webHookAlarmConfig.setBindPort(instanceConfig.getServerPort());
        webHookAlarmConfig.setServerId(instanceConfig.getServerId());

        log.info("解析后配置如下:{}", JsonUtil.toJson(webHookAlarmConfig));
        return webHookAlarmConfig;
    }

}
