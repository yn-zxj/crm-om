package crm.om.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import crm.om.annotation.Log;
import crm.om.enums.BusinessType;
import crm.om.model.DictDataInfo;
import crm.om.model.DictTypeInfo;
import crm.om.param.dict.*;
import crm.om.service.IDictDataService;
import crm.om.service.IDictTypeService;
import crm.om.vo.PageVO;
import crm.om.vo.Result;
import crm.om.vo.dict.DictDataVo;
import crm.om.vo.dict.DictTypeVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@CrossOrigin
@RestController
@Tag(name = "字典管理")
@ApiSupport(order = 900)
@RequiredArgsConstructor
@RequestMapping(value = "/dict", produces = "application/json;charset=UTF-8")
public class DictController {

    private final IDictTypeService dictTypeService;
    private final IDictDataService dictDataService;

    /**
     * 获取全部字典类型
     *
     * @return 字典类型信息
     */
    @Operation(summary = "查询字典类型")
    @GetMapping("/type")
    @ApiOperationSupport(order = 905)
    @Parameters({
            @Parameter(name = "dictId", description = "字典ID", example = "100"),
            @Parameter(name = "dictName", description = "字典名称", example = "性别"),
            @Parameter(name = "dictType", description = "字典类型", example = "sys.gender"),
            @Parameter(name = "status", description = "状态", example = "1"),
            @Parameter(name = "current", description = "当前页", required = true, example = "1"),
            @Parameter(name = "size", description = "每页显示条数", required = true, example = "10")
    })
    public Result<PageVO<DictTypeVo>> fetchDictType(
            @RequestParam(required = false) String dictId,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String status,
            @RequestParam Integer current,
            @RequestParam Integer size) {
        LambdaQueryWrapper<DictTypeInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(dictId), DictTypeInfo::getDictId, dictId)
                .like(StringUtils.isNotBlank(dictName), DictTypeInfo::getDictName, dictName)
                .eq(StringUtils.isNotBlank(dictType), DictTypeInfo::getDictType, dictType)
                .eq(StringUtils.isNotBlank(status), DictTypeInfo::getStatus, status)
                .orderByDesc(DictTypeInfo::getCreateTime);

        Page<DictTypeInfo> infoPage = dictTypeService.page(new Page<>(current, size), wrapper);

        // 遍历集合中每个Bean，复制其属性到另一个类型的对象中，最后返回一个新的List
        List<DictTypeVo> configReqs = BeanUtil.copyToList(infoPage.getRecords(), DictTypeVo.class);

        PageVO<DictTypeVo> pageVO = PageVO.<DictTypeVo>builder()
                .pages(infoPage.getPages())
                .total(infoPage.getTotal())
                .records(configReqs)
                .build();
        return Result.ok(pageVO);
    }

    /**
     * 删除字典类型信息
     *
     * @param dictTypeParam 字典类型ID集合
     * @return 操作反馈
     */
    @Operation(summary = "删除字典类型信息")
    @ApiOperationSupport(order = 920)
    @PostMapping("/type/del")
    @Log(title = "删除字典类型信息", businessType = BusinessType.DELETE)
    public Result<Boolean> delById(@RequestBody DictTypeParam dictTypeParam) {
        boolean flag = false;

        if (!dictTypeParam.getId().isEmpty()) {
            flag = dictTypeService.removeBatchByIds(dictTypeParam.getId());
        }
        return Result.ok(flag);
    }

    /**
     * 获取字典数据
     *
     * @return 字典数据信息
     */
    @Operation(summary = "查询字典数据")
    @GetMapping("/data")
    @ApiOperationSupport(order = 925)
    @Parameters({
            @Parameter(name = "dictCode", description = "字典ID", example = "100"),
            @Parameter(name = "dictType", description = "字典类型", example = "sys.gender")
    })
    public Result<List<DictDataVo>> fetchDictData(
            @RequestParam(required = false) String dictCode,
            @RequestParam(required = false) String dictType
    ) {
        LambdaQueryWrapper<DictDataInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(dictCode), DictDataInfo::getDictCode, dictCode)
                .eq(StringUtils.isNotBlank(dictType), DictDataInfo::getDictType, dictType)
                .orderByAsc(DictDataInfo::getDictSort);

        List<DictDataInfo> list = dictDataService.list(wrapper);

        List<DictDataVo> dictDataVo = BeanUtil.copyToList(list, DictDataVo.class);
        return Result.ok(dictDataVo);
    }

    /**
     * 删除字典数据信息
     *
     * @param dictDataParam 字典数据ID集合
     * @return 操作反馈
     */
    @Operation(summary = "删除字典数据信息")
    @ApiOperationSupport(order = 930)
    @PostMapping("/data/del")
    @Log(title = "删除字典数据信息", businessType = BusinessType.DELETE)
    public Result<Boolean> delById(@RequestBody DictDataParam dictDataParam) {
        boolean flag = false;

        if (!dictDataParam.getId().isEmpty()) {
            flag = dictDataService.removeBatchByIds(dictDataParam.getId());
        }
        return Result.ok(flag);
    }

    @Operation(summary = "新增字典类型")
    @ApiOperationSupport(order = 935)
    @PostMapping("/type/add")
    @Log(title = "新增字典类型", businessType = BusinessType.INSERT)
    public Result<Boolean> typeAdd(@RequestBody DictTypeAddParam dictTypeParam) {
        DictTypeInfo dictTypeInfo = DictTypeInfo.builder()
                .dictName(dictTypeParam.getDictName())
                .remark(dictTypeParam.getRemark())
                .dictType(dictTypeParam.getDictType())
                .status(dictTypeParam.getStatus())
                .build();
        return Result.ok(dictTypeService.save(dictTypeInfo));
    }

    @Operation(summary = "修改字典类型")
    @ApiOperationSupport(order = 940)
    @PostMapping("/type/edit")
    @Log(title = "修改字典类型", businessType = BusinessType.UPDATE)
    public Result<Boolean> typeEdit(@RequestBody DictTypeUpdateParam dictTypeUpdateParam) {
        LambdaUpdateWrapper<DictTypeInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StringUtils.isNotBlank(dictTypeUpdateParam.getDictName()), DictTypeInfo::getDictName, dictTypeUpdateParam.getDictName())
                .set(StringUtils.isNotBlank(dictTypeUpdateParam.getDictType()), DictTypeInfo::getDictType, dictTypeUpdateParam.getDictType())
                .set(StringUtils.isNotBlank(dictTypeUpdateParam.getStatus()), DictTypeInfo::getStatus, dictTypeUpdateParam.getStatus())
                .set(StringUtils.isNotBlank(dictTypeUpdateParam.getRemark()), DictTypeInfo::getRemark, dictTypeUpdateParam.getRemark())
                .eq(DictTypeInfo::getDictId, dictTypeUpdateParam.getDictId());

        return Result.ok(dictTypeService.update(updateWrapper));
    }

    @Operation(summary = "新增字典数据")
    @ApiOperationSupport(order = 945)
    @PostMapping("/data/add")
    @Log(title = "新增字典数据", businessType = BusinessType.INSERT)
    public Result<Boolean> dataAdd(@RequestBody DictDataAddParam dictDataParam) {
        DictDataInfo dictTypeInfo = DictDataInfo.builder()
                .dictType(dictDataParam.getDictType())
                .dictSort(dictDataParam.getDictSort())
                .dictLabel(dictDataParam.getDictLabel())
                .dictValue(dictDataParam.getDictValue())
                .status(dictDataParam.getStatus())
                .remark(dictDataParam.getRemark())
                .build();
        return Result.ok(dictDataService.save(dictTypeInfo));
    }

    @Operation(summary = "修改字典数据")
    @ApiOperationSupport(order = 950)
    @PostMapping("/data/edit")
    @Log(title = "修改字典数据", businessType = BusinessType.UPDATE)
    public Result<Boolean> dataEdit(@RequestBody DictDataUpdateParam dictDataUpdateParam) {
        LambdaUpdateWrapper<DictDataInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StringUtils.isNotBlank(dictDataUpdateParam.getDictType()), DictDataInfo::getDictType, dictDataUpdateParam.getDictType())
                .set(StringUtils.isNotBlank(dictDataUpdateParam.getDictSort()), DictDataInfo::getDictSort, dictDataUpdateParam.getDictSort())
                .set(StringUtils.isNotBlank(dictDataUpdateParam.getDictLabel()), DictDataInfo::getDictLabel, dictDataUpdateParam.getDictLabel())
                .set(StringUtils.isNotBlank(dictDataUpdateParam.getDictValue()), DictDataInfo::getDictValue, dictDataUpdateParam.getDictValue())
                .set(StringUtils.isNotBlank(dictDataUpdateParam.getStatus()), DictDataInfo::getStatus, dictDataUpdateParam.getStatus())
                .set(StringUtils.isNotBlank(dictDataUpdateParam.getRemark()), DictDataInfo::getRemark, dictDataUpdateParam.getRemark())
                .eq(DictDataInfo::getDictCode, dictDataUpdateParam.getDictCode());

        return Result.ok(dictDataService.update(updateWrapper));
    }
}
