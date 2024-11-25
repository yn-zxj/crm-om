<script setup lang="ts">
import { AttrTab, BaseTab, CrmTab, MarketTab, ProdTab, RuleTab } from './components';
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NGrid :item-responsive="true">
      <NGridItem span="24">
        <NCard title="资费基本信息" class="sm:flex-1-hidden card-wrapper" content-style="overflow:hidden" hoverable>
          <NTabs type="line" animated>
            <NTabPane name="base" tab="基础域最值">
              <BaseTab />
            </NTabPane>
            <NTabPane name="prod" tab="产品库最值">
              <ProdTab />
            </NTabPane>
            <NTabPane name="crm" tab="用户库最值">
              <CrmTab />
            </NTabPane>
            <NTabPane name="market" tab="营销库最值">
              <MarketTab />
            </NTabPane>
            <NTabPane name="prcRule" tab="产品规则">
              <RuleTab />
            </NTabPane>
            <NTabPane name="PrcAttr" tab="资费属性">
              <AttrTab />
            </NTabPane>
          </NTabs>
        </NCard>
      </NGridItem>
    </NGrid>
  </div>
</template>

<style scoped></style>
