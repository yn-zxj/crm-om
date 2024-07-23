package crm.om.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.enums.Platform;
import crm.om.enums.ResultCode;
import crm.om.exception.BaseException;
import crm.om.model.ConfigInfo;
import crm.om.param.prod.ProdParam;
import crm.om.service.IProdService;
import crm.om.utils.FreemarkerUtil;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @Operation(summary = "基础信息")
    public Result<Object> baseInfo() {
        return null;
    }

    @PostMapping("/configScript")
    @Operation(summary = "配置脚本")
    public Result<String> prodConfig(@RequestBody ProdParam prodParam) {
        ConfigInfo info = ConfigInfo.builder()
                .platform(prodParam.getPlatform().getDesc())
                .env(prodParam.getEnv().getCode())
                .build();
        Map<String, Object> result = prodService.prodConfig(info, prodParam.getPrcId());
        try {
            String template;
            switch (prodParam.getPlatform()) {
                case Platform.BSS -> template = "prodConfig/bss.ftl";
                case Platform.MVNE -> template = "prodConfig/mvne.ftl";
                case Platform.MVNO -> template = "prodConfig/mvno.ftl";
                case Platform.SGP -> template = "prodConfig/sgp.ftl";
                default -> template = null;
            }
            if (template == null) {
                throw new BaseException(ResultCode.TEMPLATE_NOT_FOUND);
            }
            String process = FreemarkerUtil.process(template, result);
            return Result.ok(process);
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }
}
