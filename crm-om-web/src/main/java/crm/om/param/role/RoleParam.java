package crm.om.param.role;

import crm.om.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "角色新增参数")
public class RoleParam {
    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色类型")
    private Role roleCode;

    @Schema(description = "角色描述")
    private String roleDesc;
}
