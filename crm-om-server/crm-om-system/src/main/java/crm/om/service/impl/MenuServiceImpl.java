package crm.om.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import crm.om.mapper.MenuInfoMapper;
import crm.om.model.MenuInfo;
import crm.om.service.IMenuService;
import crm.om.utils.CheckHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements IMenuService {

    private final CheckHelper checkHelper;
    private final MenuInfoMapper menuInfoMapper;

    @Override
    public String qryMenu(LambdaQueryWrapper<MenuInfo> wrapper) {
        List<MenuInfo> result = menuInfoMapper.selectList(wrapper);
        return generate(result);
    }

    /**
     * 树形路由封装
     *
     * @param menuList 菜单数据
     * @return 树形菜单json字符串
     */
    public String generate(List<MenuInfo> menuList) {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        treeNodeConfig.setWeightKey("priority");
        treeNodeConfig.setParentIdKey("parentId");
        // 最大递归深度
        treeNodeConfig.setDeep(3);
        treeNodeConfig.setChildrenKey("children");

        //转换器 (含义:找出父节点为字符串零的所有子节点, 并递归查找对应的子节点, 深度最多为 3)
        List<Tree<String>> build = TreeUtil.build(menuList, "0", treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getMenuId());
                    tree.setParentId(treeNode.getParentId());
                    tree.putExtra("name", treeNode.getRouteName());
                    tree.putExtra("path", treeNode.getRoutePath());
                    tree.putExtra("props", checkHelper.isValidJson(treeNode.getProps()) ? JSONUtil.parse(treeNode.getProps()) : treeNode.getProps());
                    tree.putExtra("component", treeNode.getComponent());
                    Map<String, Object> meta = new HashMap<>(16);
                    meta.put("hideInMenu", treeNode.getHideInMenu());
                    meta.put("href", treeNode.getHref());
                    meta.put("i18nKey", treeNode.getI18nKey());
                    meta.put("icon", treeNode.getIcon());
                    meta.put("keepAlive", treeNode.getKeepAlive());
                    meta.put("layout", treeNode.getComponent());
                    meta.put("multiTab", treeNode.getMultiTab());
                    meta.put("order", treeNode.getPriority());
                    meta.put("title", treeNode.getMenuName());
                    meta.put("constant", treeNode.getConstant());
                    tree.putExtra("meta", meta);
                });
        return JSONUtil.toJsonStr(build);
    }
}
