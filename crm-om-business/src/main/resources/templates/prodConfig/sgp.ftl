<#-- 新分 产品配置脚本模板文件 -->
-------------------------------------------------------
-- 资费编码:
-- 资费名称:
-- 产品编码:
-- 产品名称:
-- 参考资费:
-- 需求地址:
-------------------------------------------------------

-- 产品表回滚脚本
${prodSelect}
${prcSelect}
-- 产品表执行脚本
${prodInsert}
${prcInsert}<#rt>
-------------------------------------------------------

-- 国际化回滚脚本
${baseSelect}
-- 国际化执行脚本
${baseInsert}<#rt>
-------------------------------------------------------

-- 用户表回滚脚本
${crmProdSelect}
${crmPrcSelect}
-- 用户表执行脚本
${crmProdInsert}
${crmPrcInsert}<#rt>
-------------------------------------------------------

-- 营销表回滚脚本
${marketSelect}
-- 营销表执行脚本
${marketInsert}<#rt>
-------------------------------------------------------
-- 待确认问题:
