package io.github.luyongwang.dble.sender;

import com.actiontech.dble.cluster.general.bean.ClusterAlertBean;

/**
 * 发送类
 *
 * @author yongwang.lu
 */
public interface IAlarmSender {


    boolean sendAlert(boolean isResolve, ClusterAlertBean clusterAlertBean);

}
