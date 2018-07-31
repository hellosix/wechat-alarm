package com.lzz.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lzz on 17/4/8.
 */
public class Wechat {
    private static Logger logger = LoggerFactory.getLogger(Wechat.class);
    private static final int RETRY_TIME = 2; //重试次数
    private static final String corpid = PropertiesUtil.get("wechat.corpid");
    private static final String corpsecret=PropertiesUtil.get("wechat.corpsecret");
    private static final int agentid = 1;
    private static String accessToken;


    /**
     * 获取 accessToken
     * @return
     */
    public static String getAccessToken(){
        if( accessToken == null ){
            setAccessToken();
        }
        logger.info("accessToken 的值是："+accessToken);
        return accessToken;
    }

    private static boolean setAccessToken(){
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
        JSONObject res = HttpClient.httpGet(url);
        accessToken = (String) res.get("access_token");
        logger.info("重新获取 access token");
        return true;
    }

    /**
     * 检查相应结果
     * @param res
     * @return
     */
    private static boolean checkResponse(JSONObject res){
        int errcode = (int) res.get("errcode");
        if( errcode != 0 ){
            if( errcode == 40014 ){
                setAccessToken();
            }
            return false;
        }
        return true;
    }

    /**
     * 发送消息
     * @param jsonObject
     * @return
     */
    private static boolean messageSend( JSONObject jsonObject ){
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + getAccessToken();
        JSONObject res = HttpClient.httpPost( url, jsonObject, false);
        boolean responStatus = checkResponse(res);
        return responStatus;
    }

    /**
     * 发送 text 类型的 message
     * @param userList
     * @param content
     * @return
     */
    public static boolean sendTextMessage(String userList, String content){
        JSONObject msgObj = new JSONObject();
        msgObj.put("touser", userList);
        msgObj.put("toparty", "@all");
        msgObj.put("totag", "@all");
        msgObj.put("msgtype", "text");
        msgObj.put("agentid", agentid);
        msgObj.put("safe", "0");
        JSONObject contentObj = new JSONObject();
        contentObj.put("content", content);
        msgObj.put("text", contentObj);

        boolean sendStatus;
        int i = 0;
        while (true){
            sendStatus = messageSend(msgObj);
            if(sendStatus == true){
                break;
            }
            if( i == RETRY_TIME ){
                return false;
            }
            i++;
        }
        return sendStatus;
    }

    /**
     * 获取部门列表
     * @return
     */
    public static JSONObject departmentList(){
        String url = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + getAccessToken();

        JSONObject res;
        boolean resStatus;
        int i = 0;
        while (true){
            res = HttpClient.httpGet(url);
            resStatus = checkResponse(res);
            if(resStatus == true){
                break;
            }
            if( i == RETRY_TIME ){
                return null;
            }
            i++;
        }

        JSONArray departArr = (JSONArray) res.get("department");
        if( departArr.size() == 0 ){
            return null;
        }
        return res;
    }

    /**
     * 获取用户列表
      * @param departmentId
     * @return
     */
    public static JSONArray userList(int departmentId){
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + getAccessToken() + "&department_id=" + departmentId + "&fetch_child=1&status=0";
        JSONObject res;
        boolean resStatus;

        int i = 0;
        while (true){
            res = HttpClient.httpGet(url);
            resStatus = checkResponse(res);
            if(resStatus == true){
                break;
            }
            if( i == RETRY_TIME ){
                return null;
            }
            i++;
        }

        JSONArray userArr = (JSONArray) res.get("userlist");
        if( userArr.size() == 0 ){
            return null;
        }
        return userArr;
    }

    /**
     * 获取所有用户
     */
    public static JSONObject allUserList(){
        JSONObject departmentList = departmentList();
        if (departmentList == null){
            return null;
        }
        JSONArray departIds = (JSONArray) departmentList.get("department");
        JSONObject allUser = new JSONObject();
        for(int i = 0; i < departIds.size(); i++){
            JSONObject userObj = (JSONObject) departIds.get(i);
            JSONArray res = userList((Integer) userObj.get("id"));
            allUser.put( (String)userObj.get("name"), res);
            System.out.println(res);
        }
        return allUser;
    }
}
