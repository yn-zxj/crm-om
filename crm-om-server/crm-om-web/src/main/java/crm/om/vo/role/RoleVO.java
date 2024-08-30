package crm.om.vo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "角色属性")
public class RoleVO {
    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色描述")
    private String roleDesc;

    @Schema(description = "创建人")
    public String createBy;

    @Schema(description = "创建时间")
    public LocalDateTime createTime;

    @Schema(description = "修改人")
    public String updateBy;

    @Schema(description = "修改时间")
    public LocalDateTime updateTime;

    @Schema(description = "状态")
    private Boolean status;
}
