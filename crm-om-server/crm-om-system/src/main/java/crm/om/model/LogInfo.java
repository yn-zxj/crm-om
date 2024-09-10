package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
    @TableId(value = "title")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @TableId(value = "business_type")
    private Integer businessType;

    /**
     * 业务类型数组
     */
    private Integer[] businessTypes;

    /**
     * 请求方法
     */
    @TableId(value = "method")
    private String method;

    /**
     * 请求方式
     */
    @TableId(value = "request_method")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @TableId(value = "op_type")
    private Integer opType;

    /**
     * 操作人员
     */
    @TableId(value = "op_name")
    private String opName;

    /**
     * 请求url
     */
    @TableId(value = "op_url")
    private String opUrl;

    /**
     * 操作地址
     */
    @TableId(value = "op_ip")
    private String opIp;

    /**
     * 请求参数
     */
    @TableId(value = "op_param")
    private String opParam;

    /**
     * 返回参数
     */
    @TableId(value = "op_result")
    private String opResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @TableId(value = "status")
    private Integer status;

    /**
     * 错误消息
     */
    @TableId(value = "error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableId(value = "op_time")
    private Date opTime;

    /**
     * 消耗时间(毫秒)
     */
    @TableId(value = "costTime")
    private Long costTime;
}
