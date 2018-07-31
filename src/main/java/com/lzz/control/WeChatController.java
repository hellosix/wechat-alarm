package com.lzz.control;

import com.lzz.logic.WechatLogic;
import com.lzz.util.wechat.CheckUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by lzz on 2018/4/3.
 */
@Controller
public class WeChatController {
    @Resource
    private WechatLogic wechatLogic;

    /**
     * 验证微信服务器
     *
     * @param response
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     */
    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    public void wechatService(PrintWriter out, HttpServletResponse response,
                              @RequestParam(value = "signature", required = false) String signature, @RequestParam String timestamp,
                              @RequestParam String nonce, @RequestParam String echostr) {
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
    }

    /**
     * 接收来自微信发来的消息
     *
     * @param out
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public void wechatServicePost(PrintWriter out, HttpServletRequest request, HttpServletResponse response) {
        String responseMessage = wechatLogic.processRequest(request);
        out.print(responseMessage);
        out.flush();
    }
}
