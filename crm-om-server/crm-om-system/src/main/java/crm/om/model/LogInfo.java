package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志记录
 *
 * @author zhangxiaojun
 * @version 1.0
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_op_log")
public class LogInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 639446926097569306L;

    /**
     * 操作日志ID
     */
    @TableId(value = "op_id", type = IdType.ASSIGN_ID)
    private String opId;

    /**
     * 操作模块
     */
    @TableField(value = "title")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 请求方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求方式
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @TableField(value = "op_type")
    private Integer opType;

    /**
     * 操作人员
     */
    @TableField(value = "op_name")
    private String opName;

    /**
     * 请求url
     */
    @TableField(value = "op_url")
    private String opUrl;

    /**
     * 操作地址
     */
    @TableField(value = "op_ip")
    private String opIp;

    /**
     * 请求参数
     */
    @TableField(value = "op_param")
    private String opParam;

    /**
     * 返回参数
     */
    @TableField(value = "op_result")
    private String opResult;

    /**
     * 操作状态（1正常 0异常）
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField(value = "op_time")
    private LocalDateTime opTime;

    /**
     * 消耗时间(毫秒)
     */
    @TableField(value = "cost_time")
    private Long costTime;
}
