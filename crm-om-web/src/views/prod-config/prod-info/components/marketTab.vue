<script setup lang="ts">
import { onMounted, ref } from 'vue';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';

const dataSource = ref();

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
async function fetchMarketMax() {}

// 监听tab切换
onMounted(() => {
  fetchMarketMax();
});
</script>

<template>
  <NDataTable :bordered="false" :columns="columns" :data="dataSource" :max-height="450" size="small" />
</template>

<style scoped></style>
