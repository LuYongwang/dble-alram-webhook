package io.github.luyongwang.dble.sender.impl;


import com.actiontech.dble.cluster.general.bean.ClusterAlertBean;
import com.actiontech.dble.util.CollectionUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.luyongwang.dble.entity.AlertType;
import io.github.luyongwang.dble.sender.IAlarmSender;
import io.github.luyongwang.dble.util.AsyncHttpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yongwang.lu
 */
@AllArgsConstructor
@Slf4j
public class WorkWeChatHookAlarmSender implements IAlarmSender {

    private String robotId;

    private Map<String, JsonObject> dbConfig;

    private final static String WEB_HOOK_API_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";


    @Override
    public boolean sendAlert(boolean isResolve, ClusterAlertBean clusterAlertBean) {

        String instanceRobotId = robotId;

        Map<String, Object> reqData = new HashMap<>(16);
        reqData.put("msgtype", "markdown");
        String content = buildMessage(clusterAlertBean, isResolve);
        JsonObject instanceConfig = this.dbGroupConfig(clusterAlertBean);
        if (instanceConfig != null && !instanceConfig.isJsonNull()) {
            JsonElement principal = instanceConfig.get("principal");
            if (principal != null) {
                String[] principals = principal.getAsString().split(",");
                if (!ArrayUtils.isEmpty(principals)) {
                    content = content + "\n" + Arrays.stream(principals).map((item) -> "<@" + item + ">").collect(Collectors.joining());
                }
            }
            JsonElement thisRobotId = instanceConfig.get("robot_id");
            if (thisRobotId != null) {
                instanceRobotId = thisRobotId.getAsString();
            }
        }

        reqData.put("markdown", Collections.singletonMap("content", content));

        AsyncHttpUtil.asyncPost(WEB_HOOK_API_URL + "?key=" + instanceRobotId, reqData);

        return true;
    }

    private String buildMessage(ClusterAlertBean clusterAlertBean, boolean isResolve) {
        StringBuilder sb = new StringBuilder(isResolve ? "<font color='info'>DBLE报警已恢复</font>:\n" : "<font color='warning'>DBLE告警消息</font>:\n");
        sb.append(" >告警代码:").append(clusterAlertBean.getCode()).append("\n");
        sb.append(" >告警解释:").append(AlertType.valueOf(clusterAlertBean.getCode()).getMsg()).append("\n");
        if (StringUtils.isNotEmpty(clusterAlertBean.getDesc())) {
            sb.append(" >告警消息:").append(clusterAlertBean.getDesc()).append("\n");
        }
        sb.append(" >告警级别:").append(clusterAlertBean.getLevel()).append("\n");
        sb.append(" >告警节点:").append(clusterAlertBean.getServerId()).append("\n");
        sb.append(" >告警时间:").append((new DateTime(clusterAlertBean.getTimestampUnix())).toString("yyyy-MM-dd HH:mm:ss")).append("\n");
        StringBuilder detail = new StringBuilder("|");
        if (!CollectionUtil.isEmpty(clusterAlertBean.getLabels())) {
            clusterAlertBean.getLabels().forEach((key, value) -> {
                detail.append(key).append(":").append(value);
            });
            sb.append(">告警详情:").append(detail).append("|\n");
        }
        return sb.toString();
    }


    private JsonObject dbGroupConfig(ClusterAlertBean clusterAlertBean) {
        if (clusterAlertBean != null && !CollectionUtil.isEmpty(clusterAlertBean.getLabels())) {
            Map<String, String> labels = clusterAlertBean.getLabels();
            String dbInstance = labels.get("dbInstance");
            if (StringUtils.isEmpty(dbInstance)) {
                return null;
            } else {
                String dbGroupName = dbInstance.split("-")[0];
                return dbConfig.get(dbGroupName);
            }
        } else {
            return null;
        }
    }

}
