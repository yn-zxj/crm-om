<script setup lang="ts">
import { onMounted, ref } from 'vue';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';

const dataSource = ref();
const prodInput = ref('HKYFPROD,HK');

const columns: TableColumn<any>[] = [
  {
    title: '表名',
    key: 'table_name',
    align: 'left'
  },
  {
    title: '最值(测试)',
    key: 'max_test',
    align: 'left'
  },
  {
    title: '最值(生产)',
    key: 'max_prod',
    align: 'left'
  }
];

// 产品库表的最值
async function fetchProdMax(prefix: string) {}

// 监听tab切换
onMounted(() => {
  fetchProdMax(prodInput.value);
});
</script>

<template>
  <!-- 查询条件 -->
  <NSpace>
    <NFormItem show-require-mark label-width="auto" label-placement="left" size="small" label="查询内容">
      <NInput
        v-model:value="prodInput"
        placeholder="请输入查询内容(PROD_ID,ELEMENT_ID)..."
        clearable
        round
        @keypress.enter="fetchProdMax(prodInput)"
      />
    </NFormItem>
    <NButton type="success" size="small" round @click="fetchProdMax(prodInput)">查询</NButton>
  </NSpace>
  <NDataTable :bordered="false" :columns="columns" :data="dataSource" :max-height="370" size="small" />
</template>

<style scoped></style>
