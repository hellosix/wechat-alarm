package com.lzz.dao;

import com.lzz.model.LogModel;
import com.lzz.util.Common;
import com.lzz.util.CommonUtil;
import com.lzz.util.Sqlite;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lzz on 17/4/10.
 */
@Component
public class LogDao implements Common{
    public LogDao(){

    }
    public static boolean insertLogs(LogModel logs){
        int roleId = logs.getRoleid();
        String roleName = logs.getRoleName();
        String service = logs.getService();
        double metric = logs.getMetric();
        double metric_value = logs.getMetricValue();
        String errorMessage = logs.getErrorMessage();
        int errorCode = logs.getErrorCode();
        String members = logs.getMembers();
        String client_id = logs.getClientId();
        long add_time = logs.getAddTime();

        String sql = "insert into logs(roleid, role_name, service, metric,metric_value, error_message, error_code, members, client_id, add_time, day, hour, minute) " +
                "VALUES ("+ roleId+",'"+ roleName +"','"+ service+"'," +
                +metric+"," +
                +metric_value+"," +
                "'"+errorMessage+"',"+errorCode+"," +
                "'"+members+"'," + "'" + client_id+ "'," + add_time + "," +
                CommonUtil.getDay() + "," +
                CommonUtil.getHour() + "," +
                CommonUtil.getMinute() + ")";
        int res = Sqlite.getSqlite().insert(sql);
        if( res == 0 ){
            return false;
        }
        return true;
    }

    public List selectLogs(){
        String sql = "select * from logs";
        List list = Sqlite.getSqlite().select(sql);
        System.out.println(list);
        return list;
    }

    /**
     * 通过 id  删除logs
     * @param roleid
     * @return
     */
    public boolean deleteLogsByRoleid( int roleid ){
        String sql = "delete from logs where roleid=" + roleid;
        boolean res = Sqlite.getSqlite().delete( sql );
        return  res;
    }

    public List selectByRoleidGroupLogs(int roleid, String timeType, int dateTime){
        String sql = "";
        if( timeType.equals("minute") ){
            sql = "select concat(hour,':',minute) as date,count(*) as c from logs where add_time > " + dateTime+ " and roleid=" +roleid+ " group by hour," + timeType;
        }else if( timeType.equals("hour") ){
            sql = "select " + timeType + " as date,count(*) as c from logs where add_time > " + dateTime+ " and roleid=" +roleid+ " group by day," + timeType;
        }else{
            sql = "select " + timeType + " as date,count(*) as c from logs where add_time > " + dateTime+ " and roleid=" +roleid+ " group by " + timeType;
        }
        System.out.println(sql);
        List list = Sqlite.getSqlite().select(sql);
        System.out.println(list);
        return list;
    }

    public List selectByServiceGroupLogs(String service, String timeType, long dateTime, String role){
        String sql = "";
        if( timeType.equals("minute") ){
            sql = "select concat(hour,':',minute) as date,count(*) as c from logs where add_time > " + dateTime+ " and service='" + service + "' and role_name='" + role + "' group by hour," + timeType;
        }else if( timeType.equals("hour") ){
            sql = "select " + timeType + " as date,count(*) as c from logs where add_time > " + dateTime+ " and service='" + service + "' and role_name='" + role + "'  group by day," + timeType;
        }else{
            sql = "select day as date,count(*) as c from logs where add_time > " + dateTime+ " and service='" + service + "' and role_name='" + role + "'  group by day";
        }
        System.out.println(sql);
        List list = Sqlite.getSqlite().select(sql);
        System.out.println(list);
        return list;
    }

    public List selectLogsByRoleid(String roleId, long dateTime){
        String sql = "select * from logs where add_time > " + dateTime+ " and roleid='" + roleId + "' order by add_time desc limit " + LIMIT;
        List list = Sqlite.getSqlite().select( sql );
        return list;
    }

    public List selectLogsByService(String service, long dateTime){
        String sql = "select * from logs where add_time > " + dateTime+ " and service='" + service + "' order by add_time desc limit " + LIMIT;
        List list = Sqlite.getSqlite().select( sql );
        System.out.println( sql );
        return list;
    }
}
