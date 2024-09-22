package crm.om.vo.config;

import crm.om.vo.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "配置信息")
public class ConfigVo extends BaseVo {
    @Schema(description = "配置ID")
    private String configId;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键")
    private String configKey;

    @Schema(description = "参数值")
    private Object configValue;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "参数类型")
    public String configType;

    @Schema(description = "状态")
    private String status;
}
