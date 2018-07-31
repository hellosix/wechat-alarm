package com.lzz.control;

import com.lzz.dao.RoleDao;
import com.lzz.logic.LogLogic;
import com.lzz.logic.RoleLogic;
import com.lzz.model.Response;
import com.lzz.model.WarnParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lzz on 2018/4/2.
 */
@Controller
public class LogController {
    @Resource
    private RoleLogic roleLogic;
    @Resource
    private LogLogic logLogic;

    @RequestMapping("/log_statistic")
    public String list( @RequestParam(value="service", defaultValue="") String service, @RequestParam(value="timeType", defaultValue="minute") String timeType, Model model) {

        List logs = logLogic.getLogs(service, timeType, "service");
        Set<String> roles = logLogic.getRoles(logs);
        Map<String, List> metricsMap = new HashMap<>();
        for(String role : roles){
            List metrics = logLogic.getMetricGroup(service, timeType, role);
            metricsMap.put( role, metrics );
        }
        List services = roleLogic.getServices();

        model.addAttribute("service", service );
        model.addAttribute("timeType", timeType );
        model.addAttribute("role_list", roles );
        model.addAttribute("services", services );
        model.addAttribute("logs", logs );
        model.addAttribute("metricsMap", metricsMap );
        return "log_statistic";
    }


    /**
     * 报警接口，用户可以直接 curl 将数据发送过来
     */
    @RequestMapping(value="/mosquito", method = RequestMethod.POST)
    @ResponseBody
    public Response add_log(@RequestBody WarnParam queryParam){
        Response res;
        try {
            System.out.println(queryParam);
            // 根据 roleID 和传递过来的数据写入到 log
            int roleId = queryParam.getRoleId();
            RoleDao roleDao = new RoleDao();
            Map roleModel = roleDao.getRoleById( roleId );
            String service = (String) roleModel.get("service");
            String members = (String) roleModel.get("members");
            if( StringUtils.isBlank( queryParam.getRoleName()  ) ){
                queryParam.setRoleName( (String)roleModel.get("role_name") );
            }

            logLogic.addLog( roleId, service, members, queryParam);
            double metric = queryParam.getMetric();
            double metricValue = queryParam.getMetricValue();
            String message = queryParam.getErrorMessage();
            // 判断是否报警
            roleLogic.sendMessage( roleId, metric, metricValue, message,members);
            res = Response.OK();
        }catch (Exception e){
            res = new Response(1, e.getMessage(), "fail");
        }
        return res;
    }

}
