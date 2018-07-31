package com.lzz.dao;

import com.lzz.model.RoleModel;
import com.lzz.util.Common;
import com.lzz.util.CommonUtil;
import com.lzz.util.Sqlite;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/4/10.
 */
@Component
public class RoleDao implements Common{
    public RoleDao(){

    }

    /**
     * 添加一条 报警规则
     * @param roles
     * @return
     */
    public int insertRole(RoleModel roles){

        String role_name = roles.getRoleName();
        String service = roles.getService();
        String clientIp = roles.getClientId();
        String members = roles.getMembers();
        long add_time = new Date().getTime();
        long send_time = 0;
        String sql = "insert into roles(role_name, service, client_id, members, add_time, send_time) " +
                "VALUES ('"+ role_name+"','"+ service+"','"+clientIp+"','"+members+"'," + add_time + "," + send_time + ")";
        int res = Sqlite.getSqlite().insert(sql);
        return res;
    }

    /**
     * 通过 roleID 获取 roles
     * @param id
     * @return
     */
    public Map selectRoleById(int id){
        String sql = "select * from roles where id=" + id;
        Map resMap = Sqlite.getSqlite().selectRow(sql);
        return resMap;
    }

    /**
     * 通过 ID 删除 roles
     * @param id
     * @return
     */
    public boolean deleteRoleById(int id){
        String sql = "delete from roles where id=" + id;
        boolean res = Sqlite.getSqlite().delete( sql );
        return res;
    }

    /**
     * 获取 clientID 最新的roleid
     * @param clientId
     * @return
     */
    public int getLastIdRoles( String clientId ){
        String sql = "select max(id) as id from roles where client_id='" + clientId + "'";
        Map resMap = Sqlite.getSqlite().selectRow(sql);
        if( resMap.isEmpty() || resMap == null || resMap.get("id") == null){
            return  -1;
        }
        int id = (int) resMap.get("id");
        return id;
    }

    public int updateRolesSendTime(int roleid){
        String sql = "update roles set send_time=" + CommonUtil.getTime() + " where id=" + roleid;
        int updateRow = Sqlite.getSqlite().update( sql );
        return updateRow;
    }


    /**
     * 选择所有 role
     * @return
     */
    public List selectRoles( String clientId ){
        String sql = "select * from roles where client_id='" + clientId + "'";
        if( ADMIN_LIST_IP.contains(clientId) ){
            sql = "select * from roles";
        }
        List list = Sqlite.getSqlite().select(sql);
        System.out.println(list);
        return list;
    }

    /**
     *  获取 ping 类型的 roles
     * @return
     */
    public List getPingRoles(){
        String sql = "select * from roles where type='ping'";
        List list = Sqlite.getSqlite().select(sql);
        System.out.println(list);
        return list;
    }

    public Map getRoleById( Integer roleId ){
        String sql = "select * from roles where id=" + roleId;
        Map map = Sqlite.getSqlite().selectRow( sql );
        return map;
    }

    public List selectServices(){
        String sql = "select service,count(*) as c from roles group by service";
        List list = Sqlite.getSqlite().select(sql);
        return list;
    }

    public List getRoleList() {
        String sql = "select * from roles";
        List list = Sqlite.getSqlite().select( sql );
        return list;
    }

    public List getRoleByService(String service) {
        String sql = "select * from roles where service='" + service + "'";
        List list = Sqlite.getSqlite().select( sql );
        return list;
    }
}
