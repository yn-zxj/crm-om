package crm.om.controller;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.BusinessType;
import crm.om.param.datasource.DataSourceParam;
import crm.om.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Set;

/**
 * 数据源管理
 *
 * @author zhangxiaojun
 * @version 1.0
 */

@Slf4j
@CrossOrigin
@RestController
@Tag(name = "数据源管理")
@ApiSupport(order = 200)
@RequiredArgsConstructor
@RequestMapping(value = "/dataSource", produces = "application/json;charset=UTF-8")
public class DataSourceController {

    private final DataSource dataSource;
    private final DefaultDataSourceCreator dataSourceCreator;

    /**
     * 获取全部数据源名称
     *
     * @return 数据源名称列表
     */
    @Operation(summary = "获取全部数据源")
    @ApiOperationSupport(order = 201)
    @GetMapping("/all")
    public Result<Set<String>> getAllDataSources() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return Result.ok(ds.getDataSources().keySet());
    }

    /**
     * 动态添加数据源
     *
     * @param dataSourceParam 数据源信息
     * @return 数据源名称列表
     */
    @Operation(summary = "添加数据源")
    @ApiOperationSupport(order = 205)
    @PostMapping("/add")
    @Log(title = "添加系统数据源", businessType = BusinessType.INSERT)
    public Result<Set<String>> add(@Validated @RequestBody DataSourceParam dataSourceParam) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        BeanUtils.copyProperties(dataSourceParam, dataSourceProperty);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dataSourceParam.getPoolName(), dataSource);
        return Result.ok(ds.getDataSources().keySet());
    }

    /**
     * 删除数据源
     *
     * @param name 数据源名称
     * @return 成功
     */
    @Operation(summary = "删除数据源")
    @ApiOperationSupport(order = 210)
    @DeleteMapping("/remove/{name}")
    @Log(title = "删除系统数据源", businessType = BusinessType.DELETE)
    public Result<Objects> remove(@PathVariable String name) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(name);
        return Result.ok();
    }
}
