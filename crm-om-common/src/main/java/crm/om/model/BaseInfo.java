package crm.om.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体公共信息
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -4385072863585605500L;
    /**
     * 创建人
     */
    @TableField(value = "createBy")
    public String createBy;
    /**
     * 创建时间
     */
    @TableField(value = "createTime", fill = FieldFill.INSERT)
    public LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "updateBy", update = "false")
    public String updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "updateTime", fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime updateTime;
}
