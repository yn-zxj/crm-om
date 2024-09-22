package crm.om.vo;

import cn.hutool.core.date.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实体公共信息
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
public class BaseVo {

    @Schema(description = "创建人")
    public String createBy;

    @Schema(description = "创建时间")
    public DateTime createTime;

    @Schema(description = "修改人")
    public String updateBy;

    @Schema(description = "修改时间")
    public DateTime updateTime;
}
