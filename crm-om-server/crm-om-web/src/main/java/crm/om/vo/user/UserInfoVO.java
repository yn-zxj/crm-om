package crm.om.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "用户列表信息")
public class UserInfoVO {

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "性别(0-女 1-男)")
    private String userGender;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "手机号码")
    private String userPhone;

    @Schema(description = "邮箱地址")
    private String userEmail;

    @Schema(description = "创建人")
    public String createBy;

    @Schema(description = "创建时间")
    public LocalDateTime createTime;

    @Schema(description = "修改人")
    public String updateBy;

    @Schema(description = "修改时间")
    public LocalDateTime updateTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "用户角色集合")
    private List<String> roles;

    @Schema(description = "权限按钮集合")
    private List<String> buttons;
}
