package crm.om.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.MenuInfo;
import crm.om.model.UserRoleRel;
import crm.om.service.IMenuService;
import crm.om.service.IUserRoleRelService;
import crm.om.vo.Result;
import crm.om.vo.menu.RouteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 路由接口
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "页面菜单")
@ApiSupport(order = 100)
@RequiredArgsConstructor
@RequestMapping(value = "/route", produces = "application/json;charset=UTF-8")
public class RouteController {

    private final IMenuService menuService;
    private final IUserRoleRelService roleRelService;

    @Operation(summary = "获取用户路由数据")
    @ApiOperationSupport(order = 110)
    @GetMapping("/getUserRoutes")
    public Result<List<RouteVO>> getUserRoutes(@RequestHeader("Authorization") String authorizationHeader) {
        // 查询用户权限
        LambdaQueryWrapper<UserRoleRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoleRel::getUserid, "");
        // todo
        List<UserRoleRel> list = roleRelService.list(wrapper);

        // 通过权限查询对应可以访问的菜单列表
        LambdaQueryWrapper<MenuInfo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuInfo::getRouteRole, list);
        String trees = menuService.qryMenu(menuWrapper);
        log.info("user routes:{}", trees);
        return null;
    }

    /**
     * 获取固定的路由数据(不需要权限)
     *
     * @return 常量路由
     */
    @Operation(summary = "获取固定的路由数据")
    @ApiOperationSupport(order = 105)
    @GetMapping("/getConstantRoutes")
    public Result<List<RouteVO>> getConstantRoutes() {
        // 查询已启用的常量路由
        LambdaQueryWrapper<MenuInfo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuInfo::getConstant, true);
        menuWrapper.eq(MenuInfo::getStatus, true);
        String trees = menuService.qryMenu(menuWrapper);

        log.info("constant routes:{}", trees);
        return Result.ok(JSONUtil.toList(trees, RouteVO.class));
    }
}
