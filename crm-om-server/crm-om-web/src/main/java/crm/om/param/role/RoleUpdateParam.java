package crm.om.param.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "角色新增参数")
public class RoleUpdateParam {
    @NotBlank(message = "角色ID不能为空")
    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色类型")
    private String roleCode;

    @Schema(description = "角色描述")
    private String roleDesc;

    @Schema(description = "角色状态，ture-启用 false-禁用")
    private Boolean status;
}
