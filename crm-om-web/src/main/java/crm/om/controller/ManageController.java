package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.RoleInfo;
import crm.om.model.UserInfo;
import crm.om.service.IRoleService;
import crm.om.service.IUserService;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.role.RoleVO;
import crm.om.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统管理
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@CrossOrigin
@RestController
@Tag(name = "系统管理")
@ApiSupport(order = 300)
@RequiredArgsConstructor
@RequestMapping(value = "/systemManage", produces = "application/json;charset=UTF-8")
public class ManageController {

    private final IRoleService roleService;
    private final IUserService userService;

    @GetMapping("/getUserList")
    @Operation(summary = "获取用户列表")
    @Parameters({
            @Parameter(name = "userName", description = "用户名"),
            @Parameter(name = "userGender", description = "性别"),
            @Parameter(name = "nickName", description = "昵称"),
            @Parameter(name = "userPhone", description = "手机号"),
            @Parameter(name = "userEmail", description = "邮箱"),
            @Parameter(name = "userStatus", description = "用户状态"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<UserInfoVO>> getUserList(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String userGender,
            @RequestParam(required = false) String nickName,
            @RequestParam(required = false) String userPhone,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userName), UserInfo::getUserName, userName)
                .eq(StringUtils.isNotBlank(userGender), UserInfo::getUserGender, userGender)
                .eq(StringUtils.isNotBlank(nickName), UserInfo::getNickName, nickName)
                .eq(StringUtils.isNotBlank(userPhone), UserInfo::getUserPhone, userPhone)
                .eq(StringUtils.isNotBlank(userEmail), UserInfo::getUserEmail, userEmail)
                .in(UserInfo::getStatus, StringUtils.isNotBlank(status) ? status : "0,1");

        Page<UserInfo> infoPage = userService.page(new Page<>(current, size), wrapper);

        List<UserInfoVO> UserInfoVO = BeanUtil.copyToList(infoPage.getRecords(), UserInfoVO.class);
        PageVO<UserInfoVO> pageRes = PageVO.<UserInfoVO>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(UserInfoVO)
                .build();
        return Result.ok(pageRes);
    }

    @GetMapping("/getAllRoles")
    @Operation(summary = "获取所有角色")
    public Result<List<RoleVO>> getAllRoles() {
        LambdaQueryWrapper<RoleInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleInfo::getStatus, "1");

        List<RoleInfo> list = roleService.list(wrapper);
        List<RoleVO> roleResList = BeanUtil.copyToList(list, RoleVO.class);

        return Result.ok(roleResList);
    }
}
