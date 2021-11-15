package io.github.luyongwang.dble.entity;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 配置类
 *
 * @author yongwang.lu
 */
@Builder
@Data
public class WebHookAlarmConfig {

    private String serverId;

    private String bindIp;

    private int bindPort;

    /*
     * 上面是DBLE系统信息
     */

    /*
        下面是DBLE专属告警配置
     */

    private String componentId;

    private WebHookConfig webHook;


    public boolean checkConfig() {
        if (webHook == null || webHook.getType() == null) {
            return false;
        }
        // 合法webHook
        WebHookType type = webHook.getType();
        switch (type) {
            case WORK_WECHAT:
            case DING_TALK:
                if (StringUtils.isEmpty(webHook.getRobotId())) {
                    return false;
                }
                break;
            case URL:
                if (StringUtils.isEmpty(webHook.getHookUrl())) {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Data
    @AllArgsConstructor
    public static class WebHookConfig {


        /**
         * webhook type
         */
        private WebHookType type;

        /**
         * 机器人id
         */
        private String robotId;

        /**
         * webUrl
         */
        private String hookUrl;

        /**
         *
         */
        private String hookParams;

        /**
         * 负责人
         */
        private Map<String, JsonObject> dbConfig;

    }


    public enum WebHookType {

        /**
         * 机器人类型
         */
        WORK_WECHAT,
        DING_TALK,
        URL;
    }

}


