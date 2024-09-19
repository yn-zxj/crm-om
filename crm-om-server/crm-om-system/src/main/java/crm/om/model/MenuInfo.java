package crm.om.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * 菜单信息
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu_info")
public class MenuInfo extends BaseInfo {
    @Serial
    private static final long serialVersionUID = 258679350859416137L;

    /**
     * 菜单id
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private String menuId;

    /**
     * 菜单类型 (0-目录 1-菜单 2-按钮)
     */
    @TableField(value = "menu_type")
    private String menuType;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 路由名称
     */
    @TableField(value = "route_name")
    private String routeName;

    /**
     * 路由地址
     */
    @TableField(value = "route_path")
    private String routePath;

    /**
     * 组件:layout.base-公共布局 layout.blank-空白布局
     */
    @TableField(value = "component")
    private String component;

    /**
     * 路由属性
     */
    @TableField(value = "props")
    private String props;

    /**
     * 状态(0-弃用 1-启用)
     */
    @TableField(value = "status")
    private Boolean status;

    /**
     * 缓存路由(0-否 1-是)
     */
    @TableField(value = "keep_alive")
    private Boolean keepAlive;

    /**
     * 常量路由(无需登录 0-否 1-是)
     */
    @TableField(value = "constant")
    private Boolean constant;

    /**
     * 路由外部链接
     */
    @TableField(value = "href")
    private String href;

    /**
     * 隐藏路由(0-否 1-是)
     */
    @TableField(value = "hide_menu")
    private Boolean hideInMenu;

    /**
     * 国际化编码
     */
    @TableField(value = "i18n_key")
    private String i18nKey;

    /**
     * 图标类型(0-iconify 1-local)
     */
    @TableField(value = "icon_type")
    private Boolean iconType;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 共享标签页(0-否 1-是)
     */
    @TableField(value = "multi_tab")
    private Boolean multiTab;

    /**
     * 排序(数小则高)
     */
    @TableField(value = "priority")
    private String priority;

    /**
     * 父级菜单id
     */
    @TableField(value = "parent_id")
    private String parentId;
}
