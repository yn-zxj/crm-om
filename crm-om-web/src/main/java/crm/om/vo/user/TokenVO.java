package crm.om.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
public class TokenVO {
    @Schema(description = "token")
    private String token;

    @Schema(description = "刷新token")
    private String refreshToken;
}
