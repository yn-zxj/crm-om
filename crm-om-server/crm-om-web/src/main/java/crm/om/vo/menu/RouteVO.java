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
    @Schema(description = "路由集合")
    private List<Route> routes;

    @Schema(description = "首页路由")
    private String home;
}


