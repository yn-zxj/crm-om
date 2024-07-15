package crm.om.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 菜单路由(出参为null或空则不输出其字段)
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteVO {
    @Schema(description = "子路由，单级路由没有子路由")
    private List<RouteVO> children;

    @Schema(description = "路由组件，只有第一级或最后一级路由才有该属性，作为布局组件或者页面组件")
    private String component;

    @Schema(description = "路由属性描述")
    private Meta meta;

    @Schema(description = "路由名称，名称唯一")
    private String name;

    @Schema(description = "路由路径，路径唯一")
    private String path;
}


