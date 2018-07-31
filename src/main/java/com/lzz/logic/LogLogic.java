package com.lzz.logic;

import com.lzz.dao.LogDao;
import com.lzz.model.LogModel;
import com.lzz.model.WarnParam;
import com.lzz.util.ClientSign;
import com.lzz.util.CommonUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lzz on 2018/4/2.
 */
@Component
public class LogLogic {
    @Resource
    private LogDao logDao;

    public LogLogic(){

    }


    public List getMetricGroup(String target, String timeType,String role){
        List list = null;
        long dateTime = getTypeTime( timeType );
        list = logDao.selectByServiceGroupLogs(target, timeType, dateTime,role);
        return list;
    }


    public long getTypeTime( String type ){
        long time = CommonUtil.getTime();
        switch (type){
            case "minute":
                time = time - 30*60*1000;
                break;
            case "hour":
                time = time - 24*60*60*1000;
                break;
            case "day":
                time = time - 15*24*60*60*1000;
                break;
        }
        return time;
    }


    public boolean addLog(int roleId, String service, String members, WarnParam queryParam){
        LogModel logs = new LogModel();
        logs.setService(service);
        logs.setRoleid( roleId );
        logs.setRoleName( queryParam.getRoleName() );
        logs.setMetric( queryParam.getMetric() );
        logs.setMembers( members );
        logs.setClientId( ClientSign.clientIp() );
        logs.setErrorMessage( queryParam.getErrorMessage() );
        logs.setMetricValue( queryParam.getMetricValue() );
        logs.setAddTime( CommonUtil.getTime() );
        logs.setDay( CommonUtil.getDay() );
        logs.setHour( CommonUtil.getHour() );
        logs.setMinute( CommonUtil.getMinute() );
        boolean res = LogDao.insertLogs( logs );
        return  res;
    }


    public List getLogs(String target, String timeType, String type) {
        List list = null;
        long dateTime = getTypeTime( timeType );
        if( type.equals("role") ){
            list = logDao.selectLogsByRoleid(target, dateTime);
        }else {
            list = logDao.selectLogsByService(target, dateTime);
        }
        return list;
    }

    public Set getRoles(List logs) {
        Set roles = new HashSet();
        for(int i = 0; i < logs.size(); i++){
            LinkedCaseInsensitiveMap tmp = (LinkedCaseInsensitiveMap) logs.get(i);
            String roleName = (String) tmp.get("role_name");
            roles.add( roleName );
        }
        return roles;
    }
}
