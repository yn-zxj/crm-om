package crm.om.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "登录参数")
public class LoginParam {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
