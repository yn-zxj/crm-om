package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.exception.BaseException;
import crm.om.model.RoleInfo;
import crm.om.param.role.RoleParam;
import crm.om.service.IRoleService;
import crm.om.vo.Result;
import crm.om.vo.role.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理相关接口
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "角色管理")
@ApiSupport(order = 900)
@RequiredArgsConstructor
@RequestMapping(value = "/role", produces = "application/json;charset=UTF-8")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping("/save")
    @Operation(summary = "新增角色类型")
    public Result<String> insert(@RequestBody RoleParam roleParam) {
        RoleInfo roleInfo = RoleInfo.builder()
                .roleCode(roleParam.getRoleCode())
                .roleDesc(roleParam.getRoleDesc())
                .roleName(roleParam.getRoleName())
                .createBy("system")
                .status(true)
                .build();

        boolean save = roleService.save(roleInfo);

        if (save) {
            return Result.ok(roleInfo.getRoleId());
        }
        throw new BaseException("角色新增失败");
    }

    @PostMapping("/delete")
    @Operation(summary = "删除角色")
    public Result<String> delete() {
        return null;
    }

    @PostMapping("/update")
    @Operation(summary = "更新角色信息")
    public Result<String> update() {
        return null;
    }

    @PostMapping("/roleList")
    @Operation(summary = "查询全部角色信息")
    public Result<List<RoleVO>> roleList() {
        LambdaQueryWrapper<RoleInfo> wrapper = new LambdaQueryWrapper<>();

        List<RoleInfo> roleInfo = roleService.list();
        List<RoleVO> roleInfoList = BeanUtil.copyToList(roleInfo, RoleVO.class);
        return Result.ok(roleInfoList);
    }

    @GetMapping("/roleInfo")
    @Operation(summary = "查询角色信息")
    public Result<RoleVO> roleInfo(@RequestParam String roleId) {
        LambdaQueryWrapper<RoleInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleInfo::getRoleId, roleId);

        RoleInfo roleInfo = roleService.getOne(wrapper);
        RoleVO roleVO = BeanUtil.copyProperties(roleInfo, RoleVO.class);
        return Result.ok(roleVO);
    }
}
