package crm.om.vo.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "访问信息")
public class LoginVo {

    @Schema(description = "日志ID")
    private String infoId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "登录IP")
    private String ip;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    @Schema(description = "登录信息")
    private String msg;

    @Schema(description = "登录时间")
    private Date accessTime;
}
