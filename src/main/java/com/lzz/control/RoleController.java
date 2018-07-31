package com.lzz.control;

import com.lzz.logic.RoleLogic;
import com.lzz.model.Response;
import com.lzz.model.RoleModel;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class RoleController {
    @Resource
    private RoleLogic roleLogic;

    @Value("${message:Hello default}")
    private String message;

    @RequestMapping("/role")
    public String add( Model model) {
        JSONObject userList = roleLogic.getUserList();
        System.out.println(userList);
        model.addAttribute("userLsit", userList );
        List apps = roleLogic.getServices();
        System.out.println(apps);
        model.addAttribute("apps", apps );
        return "role_list";
    }

    @RequestMapping("/role/delete")
    public String deleteRoles(@RequestParam(value="roleid", defaultValue="-1") int roleid){
        roleLogic.deleteRoleDetail( roleid );
        return "redirect:/role";
    }

    /**
     * 提交 role
     * @param roleModel
     * @return
     */
    @RequestMapping(value="role/add", method = RequestMethod.POST)
    @ResponseBody
    public Response role_add(@RequestBody RoleModel roleModel){
        System.out.println(roleModel);
        roleLogic.addRole(roleModel);
        return Response.OK();
    }

    @RequestMapping(value="role/list", method = RequestMethod.GET)
    @ResponseBody
    public Response role_list(){
        List<RoleModel> list = roleLogic.roleList();
        return Response.Obj(0, list);
    }
}