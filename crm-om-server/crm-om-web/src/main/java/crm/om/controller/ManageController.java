package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.BusinessType;
import crm.om.exception.BaseException;
import crm.om.model.MenuInfo;
import crm.om.model.RoleInfo;
import crm.om.model.UserInfo;
import crm.om.param.role.RoleParam;
import crm.om.param.role.RoleUpdateParam;
import crm.om.service.IMenuService;
import crm.om.service.IRoleService;
import crm.om.service.IUserService;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.menu.MenuVO;
import crm.om.vo.menu.Route;
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
    private final IMenuService menuService;

    @GetMapping("/getUserList")
    @ApiOperationSupport(order = 305)
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
                .eq(StringUtils.isNotBlank(status), UserInfo::getStatus, status)
                .orderByDesc(UserInfo::getCreateTime);

        Page<UserInfo> infoPage = userService.page(new Page<>(current, size), wrapper);

        List<UserInfoVO> userInfoVO = BeanUtil.copyToList(infoPage.getRecords(), UserInfoVO.class);
        PageVO<UserInfoVO> pageRes = PageVO.<UserInfoVO>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(userInfoVO)
                .build();
        return Result.ok(pageRes);
    }

    /**
     * 新增角色类型
     *
     * @param roleParam 角色类型参数
     * @return 角色信息
     */
    @PostMapping("/save")
    @Log(title = "新增角色类型", businessType = BusinessType.INSERT)
    @ApiOperationSupport(order = 310)
    @Operation(summary = "新增角色类型", description = "角色类型一般很少新增，新增代码枚举需要一起维护")
    public Result<RoleVO> insert(@RequestBody RoleParam roleParam) {
        RoleInfo roleInfo = RoleInfo.builder()
                .roleCode(roleParam.getRoleCode())
                .roleDesc(roleParam.getRoleDesc())
                .roleName(roleParam.getRoleName())
                .status(roleParam.getStatus())
                .createBy("system")
                .build();
        // 成功添加
        boolean save = roleService.save(roleInfo);
        if (save) {
            RoleVO roleVO = BeanUtil.copyProperties(roleInfo, RoleVO.class);
            return Result.ok(roleVO);
        }
        throw new BaseException("角色新增失败");
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 默认返回成功
     */
    @DeleteMapping("/del/role/{id}")
    @ApiOperationSupport(order = 320)
    @Operation(summary = "删除角色")
    @Log(title = "删除角色", businessType = BusinessType.DELETE)
    @Parameter(name = "id", description = "角色ID", required = true, example = "181828")
    public Result<Boolean> delete(@PathVariable String id) {
        boolean result = roleService.removeById(id);
        return Result.ok(result);
    }

    /**
     * 更新角色信息
     *
     * @param roleUpdateParam 角色更新信息
     * @return 更新结果
     */
    @PostMapping("/role/update")
    @Log(title = "更新角色信息", businessType = BusinessType.UPDATE)
    @ApiOperationSupport(order = 315)
    @Operation(summary = "更新角色信息")
    public Result<Boolean> update(@RequestBody RoleUpdateParam roleUpdateParam) {
        LambdaUpdateWrapper<RoleInfo> wrapper = new LambdaUpdateWrapper<>();

        wrapper.set(StringUtils.isNotBlank(roleUpdateParam.getRoleName()), RoleInfo::getRoleName,
                        roleUpdateParam.getRoleName())
                .set(StringUtils.isNotBlank(roleUpdateParam.getRoleCode()), RoleInfo::getRoleCode,
                        roleUpdateParam.getRoleCode())
                .set(StringUtils.isNotBlank(roleUpdateParam.getRoleDesc()), RoleInfo::getRoleDesc,
                        roleUpdateParam.getRoleDesc())
                .set(roleUpdateParam.getStatus() != null, RoleInfo::getStatus, roleUpdateParam.getStatus())
                .eq(RoleInfo::getRoleId, roleUpdateParam.getRoleId());

        boolean result = roleService.update(wrapper);
        return Result.ok(result);
    }

    /**
     * 查询角色信息
     *
     * @param roleId   角色ID
     * @param roleCode 角色编码
     * @param status   角色状态
     * @param current  当前页
     * @param size     每页显示条数
     * @return 角色信息
     */
    @GetMapping("/getRoleList")
    @ApiOperationSupport(order = 307)
    @Operation(summary = "查询角色信息", description = "区分:<br/>1.角色状态可选;<br/>2.分页。")
    @Parameters({
            @Parameter(name = "roleId", description = "角色ID", example = "181828"),
            @Parameter(name = "roleCode", description = "角色CODE", example = "ROLE_COMMON"),
            @Parameter(name = "roleCode", description = "角色名称", example = "超级管理员"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<RoleVO>> roleList(
            @RequestParam(required = false) String roleId,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<RoleInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(roleId), RoleInfo::getRoleId, roleId)
                .eq(StringUtils.isNotBlank(roleCode), RoleInfo::getRoleCode, roleCode)
                .eq(StringUtils.isNotBlank(roleName), RoleInfo::getRoleName, roleName)
                .eq(StringUtils.isNotBlank(status), RoleInfo::getStatus, status)
                .orderByDesc(RoleInfo::getCreateTime);

        Page<RoleInfo> infoPage = roleService.page(new Page<>(current, size), wrapper);
        List<RoleVO> userInfoVO = BeanUtil.copyToList(infoPage.getRecords(), RoleVO.class);
        PageVO<RoleVO> pageRes = PageVO.<RoleVO>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(userInfoVO)
                .build();
        return Result.ok(pageRes);
    }

    @Operation(summary = "查询全部角色", description = "查询返回启用中的角色(前端用)")
    @ApiOperationSupport(order = 308)
    @GetMapping("/getAllRoles")
    public Result<List<RoleVO>> getAllRoles() {
        LambdaQueryWrapper<RoleInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleInfo::getStatus, "1")
                .orderByDesc(RoleInfo::getCreateTime);
        List<RoleInfo> list = roleService.list(wrapper);
        List<RoleVO> userInfoVO = BeanUtil.copyToList(list, RoleVO.class);
        return Result.ok(userInfoVO);
    }

    @Operation(summary = "获取所有页面组件", description = "返回页面名称")
    @ApiOperationSupport(order = 309)
    @GetMapping("/getAllPages")
    public Result<List<String>> getAllPages() {
        // 查询已启用的常量路由
        LambdaQueryWrapper<MenuInfo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuInfo::getStatus, true);
        menuWrapper.eq(MenuInfo::getMenuType, "1");
        List<MenuInfo> menuList = menuService.list(menuWrapper);
        List<String> filterList = menuList.stream().map(MenuInfo::getRouteName).toList();
        return Result.ok(filterList);
    }

    @Operation(summary = "获取菜单树")
    @ApiOperationSupport(order = 310)
    @GetMapping("/getMenuTree")
    public Result<List<Route>> getMenuTree() {
        // 查询已启用的常量路由
        LambdaQueryWrapper<MenuInfo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.eq(MenuInfo::getConstant, true);
        menuWrapper.eq(MenuInfo::getStatus, true);
        String trees = menuService.qryMenu(menuWrapper);

        return Result.ok(JSONUtil.toList(trees, Route.class));
    }

    @Operation(summary = "获取菜单列表")
    @ApiOperationSupport(order = 315)
    @GetMapping("/getMenuList/v2")
    public Result<MenuVO> getMenuList() {
        LambdaQueryWrapper<MenuInfo> menuWrapper = new LambdaQueryWrapper<>();
        menuWrapper.orderByAsc(MenuInfo::getMenuId);

        List<MenuInfo> list = menuService.list(menuWrapper);

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        treeNodeConfig.setWeightKey("priority");
        treeNodeConfig.setParentIdKey("parentId");
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        treeNodeConfig.setChildrenKey("children");

        // 转换器 (含义:找出父节点为字符串零的所有子节点, 并递归查找对应的子节点, 深度最多为 3)
        List<Tree<String>> build = TreeUtil.build(list, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getMenuId());
                    tree.setParentId(treeNode.getParentId());
                    tree.putExtra("status", treeNode.getStatus() ? "1" : "0");
                    tree.putExtra("menuType", treeNode.getMenuType());
                    tree.putExtra("menuName", treeNode.getMenuName());
                    tree.putExtra("routeName", treeNode.getRouteName());
                    tree.putExtra("routePath", treeNode.getRoutePath());
                    tree.putExtra("component", treeNode.getComponent());
                    tree.putExtra("i18nKey", treeNode.getI18nKey());
                    tree.putExtra("icon", treeNode.getIcon());
                    tree.putExtra("iconType", treeNode.getIconType() ? "0" : "1");
                    tree.putExtra("multiTab", treeNode.getMultiTab());
                    tree.putExtra("hideInMenu", treeNode.getHideInMenu());
                    tree.putExtra("order", treeNode.getPriority());
                    tree.putExtra("createBy", treeNode.getCreateBy());
                    tree.putExtra("createTime", treeNode.getCreateTime());
                    tree.putExtra("updateBy", treeNode.getUpdateBy());
                    tree.putExtra("updateTime", treeNode.getUpdateTime());
                });

        System.out.println("JSONUtil.toJsonStr(build) = " + JSONUtil.toJsonStr(build));

        List<MenuVO.Route> result = BeanUtil.copyToList(build, MenuVO.Route.class);
        System.out.println("result = " + JSON.toJSONString(result));
        MenuVO menuVO = new MenuVO();
        menuVO.setTotal(30L);
        menuVO.setCurrent(1L);
        menuVO.setSize(10L);
        menuVO.setRecords(result);

        return Result.ok(menuVO);
    }
}
