package crm.om.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.UserInfo;
import crm.om.param.user.LoginReq;
import crm.om.service.IUserService;
import crm.om.vo.Result;
import crm.om.vo.user.TokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "用户管理")
@ApiSupport(order = 200)
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class UserController {

    private final IUserService userService;

    /**
     * 登录验证
     *
     * @param loginReq 登录参数
     * @return 登录结果
     */
    @Operation(summary = "登录", description = "用户名+密码")
    @PostMapping("/login")
    public Result<TokenVO> login(@Valid @RequestBody LoginReq loginReq) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, loginReq.getUserName());
        UserInfo login = userService.getOne(wrapper);
        StpUtil.login(login.getUserId());
        return Result.ok(new TokenVO());
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @Operation(summary = "注销登录")
    @GetMapping("/logout")
    public Result<TokenVO> logout() {
        StpUtil.logout();
        return null;
    }
}
