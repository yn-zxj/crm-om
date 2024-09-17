package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.BusinessType;
import crm.om.model.LoginInfo;
import crm.om.service.ILoginService;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.log.LoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 日志管理
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@CrossOrigin
@RestController
@Tag(name = "系统访问日志管理")
@ApiSupport(order = 800)
@RequiredArgsConstructor
@RequestMapping(value = "/login", produces = "application/json;charset=UTF-8")
public class LoginController {

    private final ILoginService loginService;

    /**
     * 获取全部参数配置信息
     *
     * @return 参数信息
     */
    @Operation(summary = "查询日志")
    @GetMapping("/all")
    @ApiOperationSupport(order = 805)
    @Parameters({
            @Parameter(name = "infoId", description = "日志ID", example = "100"),
            @Parameter(name = "userId", description = "用户ID", example = "2001"),
            @Parameter(name = "status", description = "登录状态", example = "1"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<LoginVo>> fetchAll(
            @RequestParam(required = false) String infoId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<LoginInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(infoId), LoginInfo::getInfoId, infoId)
                .eq(StringUtils.isNotBlank(userId), LoginInfo::getUserId, userId)
                .eq(StringUtils.isNotBlank(status), LoginInfo::getStatus, status)
                .orderByDesc(LoginInfo::getAccessTime);

        Page<LoginInfo> infoPage = loginService.page(new Page<>(current, size), wrapper);

        // 遍历集合中每个Bean，复制其属性到另一个类型的对象中，最后返回一个新的List
        List<LoginVo> configReqs = BeanUtil.copyToList(infoPage.getRecords(), LoginVo.class);

        PageVO<LoginVo> pageVO = PageVO.<LoginVo>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(configReqs)
                .build();
        return Result.ok(pageVO);
    }

    /**
     * 删除日志信息
     *
     * @param id 日志ID
     * @return 操作反馈
     */
    @Operation(summary = "删除日志信息")
    @ApiOperationSupport(order = 820)
    @DeleteMapping("/del/{ids}")
    @Log(title = "删除访问日志", businessType = BusinessType.DELETE)
    @Parameter(name = "ids", description = "日志ID", required = true, example = "1")
    public Result<Boolean> delById(@PathVariable("ids") String ids) {
        boolean flag = false;

        String[] list = ids.split(",");
        if (list.length > 0) {
            flag = loginService.removeBatchByIds(Arrays.asList(list));
        }
        return Result.ok(flag);
    }
}
