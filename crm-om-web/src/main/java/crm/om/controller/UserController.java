package crm.om.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.enums.ResultCode;
import crm.om.enums.Role;
import crm.om.exception.BaseException;
import crm.om.model.UserInfo;
import crm.om.model.UserRoleRel;
import crm.om.param.user.LoginParam;
import crm.om.param.user.RegisterParam;
import crm.om.param.user.UpdateUserParam;
import crm.om.service.IUserRoleRelService;
import crm.om.service.IUserService;
import crm.om.vo.Result;
import crm.om.vo.user.TokenVO;
import crm.om.vo.user.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
@ApiSupport(order = 600)
@RequiredArgsConstructor
@RequestMapping(value = "/auth", produces = "application/json;charset=UTF-8")
public class UserController {

    private final IUserService userService;
    private final IUserRoleRelService userRoleRelService;

    /**
     * 登录验证
     *
     * @param loginParam 登录参数
     * @return 登录结果
     */
    @Operation(summary = "登录", description = "用户名+密码")
    @ApiOperationSupport(order = 605)
    @PostMapping("/login")
    public Result<TokenVO> login(@Valid @RequestBody LoginParam loginParam) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, loginParam.getUserName())
                .eq(UserInfo::getPassword, SecureUtil.md5(loginParam.getPassword()));

        UserInfo login = userService.getOne(wrapper);
        if (login != null) {
            StpUtil.login(login.getUserId());
            String tokenValue = StpUtil.getTokenInfo().tokenValue;
            TokenVO token = new TokenVO();
            token.setToken(tokenValue);
            token.setRefreshToken(tokenValue);
            return Result.ok(token);
        }
        throw new BaseException(ResultCode.NO_USER);
    }

    /**
     * 用户注册
     *
     * @param registerInfo 用户信息
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @ApiOperationSupport(order = 620)
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterParam registerInfo) {
        // 校验用户是否存在
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, registerInfo.getUserName())
                .eq(UserInfo::getPassword, SecureUtil.md5(registerInfo.getPassword()));
        UserInfo newUser = userService.getOne(wrapper);
        if (newUser != null) {
            throw new BaseException(ResultCode.ALREADY_USER);
        }

        // 数据落表
        UserInfo userInfo = UserInfo.builder()
                .userName(registerInfo.getUserName())
                .userGender(registerInfo.getUserGender())
                .nickName(registerInfo.getNickName())
                .userPhone(registerInfo.getUserPhone())
                .userEmail(registerInfo.getUserEmail())
                .password(SecureUtil.md5(registerInfo.getPassword()))
                .createBy("system")
                .build();
        boolean save = userService.save(userInfo);
        if (save) {
            // 用户与角色关联
            List<UserRoleRel> userRoleRelList = new ArrayList<>();
            for (Role role : registerInfo.getRoleList()) {
                UserRoleRel userRoleRel = UserRoleRel.builder()
                        .roleId(String.valueOf(role))
                        .userid(userInfo.getUserId())
                        .build();
                userRoleRelList.add(userRoleRel);
            }
            boolean result = userRoleRelService.saveBatch(userRoleRelList);
            if (result) {
                return Result.ok(userInfo);
            }
            throw new BaseException("用户与角色关系创建失败");
        } else {
            throw new BaseException("用户创建失败");
        }
    }

    /**
     * 用户删除
     *
     * @param id 用户id
     * @return 删除结果
     */
    @Operation(summary = "用户删除")
    @ApiOperationSupport(order = 630)
    @DeleteMapping("/del/{id}")
    @Parameter(name = "id", description = "用户id")
    public Result<Boolean> delUser(@PathVariable String id) {
        // 删除用户表
        userService.removeById(id);
        // 删除用户角色关系表
        LambdaQueryWrapper<UserRoleRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleRel::getUserid, id);
        boolean result = userRoleRelService.remove(wrapper);
        return Result.ok(result);
    }

    /**
     * 用户更新
     *
     * @param userInfo 用户信息
     * @return 更新结果
     */
    @Operation(summary = "用户更新")
    @ApiOperationSupport(order = 625)
    @PutMapping("/update/user")
    public Result<Boolean> updateUser(@Valid @RequestBody UpdateUserParam userInfo) {
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(!userInfo.getUserName().isBlank(), UserInfo::getUserName, userInfo.getUserName())
                .set(!userInfo.getPassword().isBlank(), UserInfo::getPassword, SecureUtil.md5(userInfo.getPassword()))
                .set(!userInfo.getUserPhone().isBlank(), UserInfo::getUserPhone, userInfo.getUserPhone())
                .set(!userInfo.getUserEmail().isBlank(), UserInfo::getUserEmail, userInfo.getUserEmail())
                .set(!userInfo.getNickName().isBlank(), UserInfo::getNickName, userInfo.getNickName())
                .set(userInfo.getUserGender() != null, UserInfo::getUserGender, userInfo.getUserGender())
                .eq(UserInfo::getUserId, userInfo.getUserId());

        // 更新用户信息
        userService.update(wrapper);
        // 更新用户与角色关联信息
        LambdaQueryWrapper<UserRoleRel> delWrapper = new LambdaQueryWrapper<>();
        delWrapper.eq(UserRoleRel::getUserid, userInfo.getUserId());
        userRoleRelService.remove(delWrapper);

        List<UserRoleRel> userRoleRelList = new ArrayList<>();
        for (Role role : userInfo.getRoleList()) {
            UserRoleRel userRoleRel = UserRoleRel.builder()
                    .roleId(String.valueOf(role))
                    .userid(userInfo.getUserId())
                    .build();
            userRoleRelList.add(userRoleRel);
        }

        boolean result = userRoleRelService.saveBatch(userRoleRelList);
        return Result.ok(result);
    }

    @Operation(summary = "用户信息")
    @ApiOperationSupport(order = 615)
    @GetMapping("/getUserInfo")
    public Result<UserInfoVO> getUserInfo() {
        UserInfoVO userInfoVO = new UserInfoVO();
        String loginId = (String) StpUtil.getTokenInfo().loginId;
        if (loginId != null) {
            System.out.println("loginId = " + loginId);
            LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(UserInfo::getUserId, loginId);
            UserInfo userInfo = userService.getOne(wrapper);
            userInfoVO = BeanUtil.copyProperties(userInfo, UserInfoVO.class);
        }
        return Result.ok(userInfoVO);
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @Operation(summary = "注销登录")
    @ApiOperationSupport(order = 610)
    @GetMapping("/logout")
    public Result<Object> logout() {
        StpUtil.logout();
        return Result.ok();
    }
}
