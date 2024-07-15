package crm.om.param.user;

import crm.om.enums.Gender;
import crm.om.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
public class RegisterReq {
    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户性别")
    private Gender userGender;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户手机号")
    private String userPhone;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "角色列表")
    private List<Role> roleList;
}
