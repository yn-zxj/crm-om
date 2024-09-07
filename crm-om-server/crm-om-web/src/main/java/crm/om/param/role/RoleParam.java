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
public class RoleParam {
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "角色类型不能为空")
    @Schema(description = "角色类型")
    private String roleCode;

    @NotBlank(message = "角色描述不能为空")
    @Schema(description = "角色描述")
    private String roleDesc;

    @NotBlank(message = "角色状态不能为空")
    @Schema(description = "角色状态")
    private String status;
}
