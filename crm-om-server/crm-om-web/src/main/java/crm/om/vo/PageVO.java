package crm.om.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 分页出参
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Data
@Builder
@Schema(description = "分页信息")
public class PageVO<T> {
    @Schema(description = "数据列表")
    private List<T> records;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "总记录数")
    private Long total;
}
