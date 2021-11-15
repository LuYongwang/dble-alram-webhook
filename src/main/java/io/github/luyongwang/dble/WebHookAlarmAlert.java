package io.github.luyongwang.dble;

import com.actiontech.dble.cluster.general.bean.ClusterAlertBean;
import com.actiontech.dble.config.util.ConfigException;
import io.github.luyongwang.dble.config.AlarmConfiguration;
import io.github.luyongwang.dble.entity.WebHookAlarmConfig;
import io.github.luyongwang.dble.sender.IAlarmSender;
import io.github.luyongwang.dble.sender.impl.DingTalkWebHookAlarmSender;
import io.github.luyongwang.dble.sender.impl.WebUrlHookAlarmSender;
import io.github.luyongwang.dble.sender.impl.WorkWeChatHookAlarmSender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yongwang.lu
 */
@Slf4j
public class WebHookAlarmAlert implements com.actiontech.dble.alarm.Alert {

    private static WebHookAlarmConfig webHookAlarmConfig;

    private static IAlarmSender alarmSender = null;

    private final static String COMPONENT_TYPE = "DBLE告警";

    public WebHookAlarmAlert() {
        /*
         * 获取配置文件
         */
        webHookAlarmConfig = AlarmConfiguration.getWebHookAlarmConfig();
        WebHookAlarmConfig.WebHookConfig webHookConfig = webHookAlarmConfig.getWebHook();
        if (webHookConfig != null) {
            WebHookAlarmConfig.WebHookType type = webHookConfig.getType();
            switch (type) {
                case WORK_WECHAT:
                    alarmSender = new WorkWeChatHookAlarmSender(webHookConfig.getRobotId(), webHookConfig.getDbConfig());
                    break;
                case DING_TALK:
                    alarmSender = new DingTalkWebHookAlarmSender(webHookConfig.getRobotId(), webHookConfig.getDbConfig());
                    break;
                case URL:
                    alarmSender = new WebUrlHookAlarmSender(webHookConfig.getHookUrl(), webHookConfig.getHookParams(), webHookConfig.getDbConfig());
                    break;
                default:
                    log.error("DBLE Webhook告警器初始化失败");
            }
        }
    }

    @Override
    public void alertSelf(ClusterAlertBean clusterAlertBean) {
        alert(clusterAlertBean.setAlertComponentType(COMPONENT_TYPE).setAlertComponentId(webHookAlarmConfig.getComponentId()));
    }

    @Override
    public void alert(ClusterAlertBean clusterAlertBean) {
        clusterAlertBean.setSourceComponentType(COMPONENT_TYPE).
                setSourceComponentId(webHookAlarmConfig.getComponentId()).
                setServerId(webHookAlarmConfig.getServerId()).
                setTimestampUnix(System.currentTimeMillis());
        alarmSender.sendAlert(false, clusterAlertBean);
    }

    @Override
    public boolean alertResolve(ClusterAlertBean clusterAlertBean) {
        clusterAlertBean.setSourceComponentType(COMPONENT_TYPE).
                setSourceComponentId(webHookAlarmConfig.getComponentId()).
                setServerId(webHookAlarmConfig.getServerId()).
                setTimestampUnix(System.currentTimeMillis());
        return alarmSender.sendAlert(false, clusterAlertBean);
    }

    @Override
    public boolean alertSelfResolve(ClusterAlertBean clusterAlertBean) {
        return alertResolve(clusterAlertBean.setAlertComponentType(COMPONENT_TYPE).setAlertComponentId(webHookAlarmConfig.getComponentId()));
    }

    @Override
    public void alertConfigCheck() {
        //check if the config is correct
        if (!webHookAlarmConfig.checkConfig()) {
            throw new ConfigException("alert config check error, Please read the README.md document");
        }
    }

}
