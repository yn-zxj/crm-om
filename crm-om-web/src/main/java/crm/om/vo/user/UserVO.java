package crm.om.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "用户信息")
public class UserVO {
    @Schema(description = "用户按钮编码集合")
    private List<String> buttons;

    @Schema(description = "用户角色集合")
    private List<String> roles;

    @Schema(description = "id")
    private String userId;

    @Schema(description = "用户名")
    private String userName;
}
