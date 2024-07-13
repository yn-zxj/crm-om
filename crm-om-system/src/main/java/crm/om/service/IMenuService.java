package crm.om.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import crm.om.model.system.MenuInfo;

/**
 * @author zhangxiaojun
 * @version 1.0
 */
public interface IMenuService extends IService<MenuInfo> {
    /**
     * 菜单查询
     *
     * @param wrapper 查询条件
     * @return 菜单数据JSON字符串
     */
    String qryMenu(LambdaQueryWrapper<MenuInfo> wrapper);
}
