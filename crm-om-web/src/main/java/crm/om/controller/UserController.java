package crm.om.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.enums.ResultCode;
import crm.om.exception.BaseException;
import crm.om.model.UserInfo;
import crm.om.param.user.LoginReq;
import crm.om.param.user.RegisterReq;
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
     * 用户注册
     *
     * @param registerInfo 用户信息
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterReq registerInfo) {
        // 校验用户是否存在
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, registerInfo.getUserName());
        UserInfo newUser = userService.getOne(wrapper);
        if (newUser != null) {
            throw new BaseException(ResultCode.ALREADY_USER);
        }

        // 数据落表
        UserInfo userInfo = UserInfo.builder()
                .userName(registerInfo.getUserName())
                .userGender(registerInfo.getUserGender())
                .userPhone(registerInfo.getUserPhone())
                .userEmail(registerInfo.getUserEmail())
                .password(registerInfo.getPassword())
                .build();
        boolean save = userService.save(userInfo);
        if (save) {
            // 用户与角色关联
            return null;
        } else {
            throw new BaseException(ResultCode.FAIL);
        }
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
