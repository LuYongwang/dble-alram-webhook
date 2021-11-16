package io.github.luyongwang.dble.sender.impl;


import com.actiontech.dble.cluster.general.bean.ClusterAlertBean;
import com.actiontech.dble.util.CollectionUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.luyongwang.dble.entity.AlertType;
import io.github.luyongwang.dble.sender.IAlarmSender;
import io.github.luyongwang.dble.util.AsyncHttpUtil;
import io.github.luyongwang.dble.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yongwang.lu
 */
@AllArgsConstructor
@Slf4j
public class FeiShuWebHookAlarmSender implements IAlarmSender {

    private String robotId;

    private Map<String, JsonObject> dbConfig;

    private final static String WEB_HOOK_API_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/";


    @Override
    public boolean sendAlert(boolean isResolve, ClusterAlertBean clusterAlertBean) {
        String instanceRobotId = robotId;
        String[] principals = {};
        JsonObject instanceConfig = this.dbGroupConfig(clusterAlertBean);
        if (instanceConfig != null && !instanceConfig.isJsonNull()) {
            JsonElement principal = instanceConfig.get("principal");
            if (principal != null) {
                principals = principal.getAsString().split(",");
            }
            JsonElement thisRobotId = instanceConfig.get("robot_id");
            if (thisRobotId != null) {
                instanceRobotId = thisRobotId.getAsString();
            }
        }

        Map<String, Object> reqData = new HashMap<>(16);
        reqData.put("msg_type", "post");
        Map<Object, Object> post = new HashMap<>(16);
        Map<String, Object> zh = new HashMap<>(16);
        // 构建消息体
        zh.put("title", "DBLE告警");
        zh.put("content", buildMessage(clusterAlertBean, isResolve, principals));
        post.put("zh_cn", zh);
        Map<Object, Object> content = new HashMap<>(16);
        content.put("post", post);
        reqData.put("content", content);
        AsyncHttpUtil.asyncPost(WEB_HOOK_API_URL + instanceRobotId, reqData);
        return true;
    }

    private List<JsonArray> buildMessage(ClusterAlertBean clusterAlertBean, boolean isResolve, String[] principals) {
        StringBuilder builder = new StringBuilder(isResolve ? "[[{\"tag\":\"text\",\"text\":\"DBLE报警已恢复\"}]," : "[[{\"tag\":\"text\",\"text\":\"DBLE告警消息\"}],");
        builder.append("[{\"tag\":\"text\",\"text\":\"告警代码: \"},{\"tag\":\"text\",\"text\":\"").append(clusterAlertBean.getCode()).append("\"}],");
        builder.append("[{\"tag\":\"text\",\"text\":\"告警解释: \"},{\"tag\":\"text\",\"text\":\"").append(AlertType.valueOf(clusterAlertBean.getCode()).getMsg()).append("\"}],");
        if (StringUtils.isNotEmpty(clusterAlertBean.getDesc())) {
            builder.append("[{\"tag\":\"text\",\"text\":\"告警消息: \"},{\"tag\":\"text\",\"text\":\"").append(clusterAlertBean.getDesc()).append("\"}],");
        }
        builder.append("[{\"tag\":\"text\",\"text\":\"告警级别: \"},{\"tag\":\"text\",\"text\":\"").append(clusterAlertBean.getLevel()).append("\"}],");
        builder.append("[{\"tag\":\"text\",\"text\":\"告警节点: \"},{\"tag\":\"text\",\"text\":\"").append(clusterAlertBean.getSourceComponentId()).append("\"}],");
        builder.append("[{\"tag\":\"text\",\"text\":\"告警时间: \"},{\"tag\":\"text\",\"text\":\"").append((new DateTime(clusterAlertBean.getTimestampUnix())).toString("yyyy-MM-dd HH:mm:ss")).append("\"}],");
        builder.append("[{\"tag\":\"text\",\"text\":\"告警代码: \"},{\"tag\":\"text\",\"text\":\"").append(clusterAlertBean.getCode()).append("\"}],");

        StringBuilder detail = new StringBuilder("|");
        if (!CollectionUtil.isEmpty(clusterAlertBean.getLabels())) {
            clusterAlertBean.getLabels().forEach((key, value) -> {
                detail.append(key).append(":").append(value);
            });
            builder.append("[{\"tag\":\"text\",\"text\":\"告警详情: \"},{\"tag\":\"text\",\"text\":\"").append(detail).append("|").append("\"}],");
        }
        // 增加@
        for (String principal : principals) {
            builder.append("[{\"tag\":\"at\",\"user_id\":\"").append(principal).append("\"}],");
        }
        builder.append("[{\"tag\":\"text\",\"text\":\"请关注\"}]]");
        return JsonUtil.toList(builder.toString(), JsonArray.class);
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
