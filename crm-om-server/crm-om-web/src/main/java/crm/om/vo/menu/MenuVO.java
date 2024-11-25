package crm.om.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import crm.om.vo.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单列表
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MenuVO {
    @Schema(description = "数据列表")
    private List<Route> records;

    @Schema(description = "当前页")
    private Long current;

    @Schema(description = "每页条数")
    private Long size;

    @Schema(description = "总记录数")
    private Long total;

    public class Route extends BaseVo {
        @Schema(description = "菜单ID")
        private String id;

        @Schema(description = "父级菜单ID")
        private String parentId;

        @Schema(description = "状态")
        private String status;

        @Schema(description = "菜单类型")
        private String menuType;

        @Schema(description = "菜单名称")
        private String menuName;

        @Schema(description = "路由名称")
        private String routeName;

        @Schema(description = "路由地址")
        private String routePath;

        @Schema(description = "组件")
        private String component;

        @Schema(description = "国际化KEY")
        private String i18nKey;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "图标类型")
        private String iconType;

        @Schema(description = "是否多级菜单")
        private Boolean multiTab;

        @Schema(description = "是否隐藏菜单")
        private Boolean hideInMenu;

        @Schema(description = "是否激活菜单")
        private String activeMenu;

        @Schema(description = "菜单排序")
        private long order;

        @Schema(description = "子菜单")
        private List<Route> children;
    }
}


