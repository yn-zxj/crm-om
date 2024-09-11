package crm.om.vo.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Schema(description = "日志信息")
public class LogVo {

    @Schema(description = "操作日志ID")
    private String opId;

    @Schema(description = "操作模块")
    private String title;

    @Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer[] businessTypes;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer opType;

    @Schema(description = "操作人员")
    private String opName;

    @Schema(description = "请求url")
    private String opUrl;

    @Schema(description = "操作地址")
    private String opIp;

    @Schema(description = "请求参数")
    private String opParam;

    @Schema(description = "返回参数")
    private String opResult;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "操作时间")
    private Date opTime;
    
    @Schema(description = "消耗时间(毫秒)")
    private Long costTime;
}
