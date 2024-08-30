package crm.om.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.model.ConfigInfo;
import crm.om.param.prod.ProdParam;
import crm.om.service.IProdService;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 产品配置相关接口
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "产品配置")
@ApiSupport(order = 900)
@RequiredArgsConstructor
@RequestMapping(value = "/prodConfig", produces = "application/json;charset=UTF-8")
public class ProdController {

    private final IProdService prodService;

    @PostMapping("/baseInfo")
    @ApiOperationSupport(order = 910)
    @Operation(summary = "基础信息")
    public Result<Object> baseInfo() {
        return null;
    }

    @PostMapping("/configScript")
    @ApiOperationSupport(order = 905)
    @Operation(summary = "配置脚本")
    public Result<String> prodConfig(@RequestBody ProdParam prodParam) {
        ConfigInfo info = ConfigInfo.builder()
                .platform(prodParam.getPlatform())
                .env(prodParam.getEnv())
                .build();

        String result = prodService.templateToStr(info, prodParam.getPrcId());
        return Result.ok(result);
    }
}
