package com.lzz.util;

import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * Created by lzz on 17/4/9.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class WechatTest {
    @Test
    public void getAccessTokenTest(){
        String res = Wechat.getAccessToken();
        System.out.println(res);

    }

    @Test
    public void messageSendTest(){
        boolean res = Wechat.sendTextMessage("linzhouzhi|lzz363216", "报警邮件去看一下啊！！！");
        System.out.println(res);
    }

    @Test
    public void allUserListTest(){
        JSONObject res = Wechat.allUserList();
        System.out.println(res);
    }
}
