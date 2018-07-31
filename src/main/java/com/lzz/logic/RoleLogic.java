package com.lzz.logic;

import com.lzz.dao.LogDao;
import com.lzz.dao.RoleDao;
import com.lzz.model.RoleModel;
import com.lzz.util.ClientSign;
import com.lzz.util.Common;
import com.lzz.util.CommonUtil;
import com.lzz.util.Wechat;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lzz on 17/4/9.
 */
@Component
public class RoleLogic implements Common{
    @Resource
    private RoleDao roleDao;

    public RoleLogic(){

    }

    /**
     * 通过 wechat 获取成员列表
     * @return
     */
    public JSONObject getUserList(){
        JSONObject res = Wechat.allUserList();
        return res;
    }

    /**
     * 添加一条 报警规则
     * @param reqData
     * @return
     */
    public boolean addRole(RoleModel reqData){
        String clientid = ClientSign.clientIp();
        reqData.setClientId( clientid );
        int res = roleDao.insertRole(reqData);
        if( res != 0 ){
            return true;
        }
        return false;
    }

    public List<RoleModel> roleList(){
        List roleList = roleDao.getRoleList();
        return roleList;
    }

    /**
     * 获取更新时间
     * @param roleId
     * @return
     */
    public long getRoleUpdateTime(int roleId){
        Map map = roleDao.selectRoleById( roleId );
        if( map.isEmpty() || map == null ){
            return 0;
        }
        return (long) map.get("send_time");
    }

    /**
     * 根据 clientid 获取roles
     * @return
     */
    public List<RoleModel> getRoles() {
        String clientid = ClientSign.clientIp();
        List list = roleDao.selectRoles(clientid);
        return list;
    }

    public List<RoleModel> getRoles(String service) {
        List list = roleDao.getRoleByService(service);
        return list;
    }

    public List getServices(){
        List list = roleDao.selectServices();
        return list;
    }

    public int getLasteRoleId( String clientIp ){
        RoleDao roles = new RoleDao();
        int roleId = roles.getLastIdRoles( clientIp );
        return roleId;
    }

    public boolean updateSendTime(int roleid){
        int updateRow = roleDao.updateRolesSendTime( roleid );
        if( updateRow != 0 ){
            return true;
        }
        return false;
    }

    public Map getRoleDetail(int roleid){
        Map map = roleDao.selectRoleById( roleid );
        return map;
    }

    public boolean deleteRoleDetail(int roleid){
        LogDao logs = new LogDao();
        boolean flag= false;
        flag = roleDao.deleteRoleById( roleid );
        flag = logs.deleteLogsByRoleid( roleid );
        return flag;
    }



    /**
     * 发送微信报警
     * @param roleId
     */
    public void sendMessage(int roleId, double metric, double metricValue, String message, String members){
        if( metric < metricValue ){
            long sendTime = this.getRoleUpdateTime( roleId );
            long currentTime = CommonUtil.getTime();
            // 如果发送时间过了 SEND_RANGE_TIME ，那么就发送报警
            if( (currentTime - sendTime) > SEND_RANGE_TIME ){
                // 微信发送报警
                boolean sendStatus = Wechat.sendTextMessage(members, message);
                if( sendStatus == true ){
                    // 修改 roles 的发送时间
                    this.updateSendTime( roleId );
                }
            }
        }
    }



    /**
     * 根据restful post 接口获取参数转化成 roles model 对象
     * @param queryParam
     * @return
     */
    public RoleModel getRolesParam(RoleModel queryParam) {
        RoleModel roles = new RoleModel();
        String clientIp = ClientSign.clientIp();
        String roleName = clientIp + queryParam.getRoleName();
        roles.setClientId( queryParam.getClientId() );
        roles.setRoleName( roleName );
        roles.setMembers( queryParam.getMembers() );
        roles.setService( queryParam.getService() );
        roles.setAddTime( CommonUtil.getTime() );
        return roles;
    }

}
