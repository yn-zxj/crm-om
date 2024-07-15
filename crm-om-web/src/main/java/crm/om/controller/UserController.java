package crm.om.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.enums.ResultCode;
import crm.om.exception.BaseException;
import crm.om.model.system.UserInfo;
import crm.om.param.user.LoginReq;
import crm.om.param.user.RegisterReq;
import crm.om.service.IUserService;
import crm.om.utils.JwtHelper;
import crm.om.vo.Result;
import crm.om.vo.user.TokenVO;
import crm.om.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final JwtHelper jwtHelper;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录验证
     *
     * @param loginReq 登录参数
     * @return 登录结果
     */
    @Operation(summary = "登录验证", description = "用户名+密码")
    @PostMapping("/login")
    public Result<TokenVO> login(@Valid @RequestBody LoginReq loginReq) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getUserName, loginReq.getUserName());
        UserInfo login = userService.getOne(wrapper);
        if (login != null && passwordEncoder.matches(loginReq.getPassword(), login.getPassword())) {
            String token = jwtHelper.createToken(login.getUserId(), login.getUserName());
            TokenVO outDTO = new TokenVO();
            outDTO.setRefreshToken(token);
            outDTO.setToken(token);
            return Result.ok(outDTO);
        } else {
            throw new BaseException(ResultCode.NO_USER.getMessage());
        }
    }

    /**
     * 用户注册
     *
     * @param registerInfo 用户信息
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody RegisterReq registerInfo) {
        String token = jwtHelper.parseHeader(authorizationHeader);
        String authUserName = jwtHelper.parseUserName(token);

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
                .createBy(authUserName)
                .updateBy(authUserName)
                .password(passwordEncoder.encode(registerInfo.getPassword()))
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
     * 刷新token
     *
     * @param refreshToken 用于获取新token的refreshToken
     * @return token信息
     */
    @Operation(summary = "刷新token")
    @PostMapping("/refreshToken")
    @Parameter(name = "refreshToken", description = "refreshToken", required = true)
    public Result<TokenVO> refreshToken(@Valid @RequestBody String refreshToken) {
        String userName = jwtHelper.parseUserName(refreshToken);
        String userId = jwtHelper.parseUserId(refreshToken);

        String token = jwtHelper.createToken(userId, userName);
        TokenVO outDTO = new TokenVO();
        outDTO.setRefreshToken(refreshToken);
        outDTO.setToken(token);
        return Result.ok(outDTO);
    }

    /**
     * 获取用户信息
     *
     * @param authorizationHeader header中的Authorization
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result<UserVO> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtHelper.parseHeader(authorizationHeader);
        String userId = jwtHelper.parseUserId(token);
        String userName = jwtHelper.parseUserName(token);

        // 查询用户是否存在
        UserInfo qryUser = userService.qryUserInfo(UserInfo.builder().userName(userName).userId(userId).build());
        if (qryUser != null) {
            // todo 查询用户按钮
            UserVO userInfo = new UserVO();
            userInfo.setUserId(qryUser.getUserId());
            userInfo.setUserName(qryUser.getUserName());
            userInfo.setRoles(qryUser.getRoleName());
            return Result.ok(userInfo);
        } else {
            throw new BaseException(ResultCode.NO_USER);
        }
    }
}
