package crm.om.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 路由属性描述(出参为null或空则不输出其字段)
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Meta {
    @Schema(description = "当前路由对应的激活状态菜单，于隐藏菜单的路由需要激活其他菜单的场景")
    private String activeMenu;

    @Schema(description = "固定在页签中的路由索引，为空时则不固定")
    private Long fixedIndexInTab;

    @Schema(description = "是否在菜单中隐藏路由")
    private Boolean hideInMenu;

    @Schema(description = "外链，菜单外部链接")
    private String href;

    @Schema(description = "国际化的展示文本，优先级高于title")
    private String i18NKey;

    @Schema(description = "iconify图标，可用于菜单和面包屑中")
    private String icon;

    @Schema(description = "是否缓存路由")
    private Boolean keepAlive;

    @Schema(description = "单级路由的布局组件")
    private String layout;

    @Schema(description = "本地图标，优先级高于icon")
    private String localIcon;

    @Schema(description = "是否支持多页签Tab，对于相同的路由路径，默认只有一个页签，开启后，支持多个")
    private Boolean multiTab;

    @Schema(description = "路由顺序")
    private Long order;

    @Schema(description = "角色集合，可以访问路由的角色集合，若为空，则所有用户都可以访问，默认超级管理员角色可以访问所有菜单")
    private List<String> roles;

    @Schema(description = "路由标题")
    private String title;

    @Schema(description = "常量路由")
    private Boolean constant;
}