<script setup lang="ts">
import { onMounted, ref } from 'vue';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';

const dataSource = ref();
const crmInput = ref();

const columns: TableColumn<any>[] = [
  {
    title: '分类编码',
    key: 'code_class',
    align: 'left'
  },
  {
    title: '编码名称',
    key: 'code_name',
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
async function fetchCrmMax() {}

// 监听tab切换
onMounted(() => {
  fetchCrmMax();
});
</script>

<template>
  <!-- 查询条件 -->
  <NSpace>
    <NFormItem show-require-mark label-width="auto" label-placement="left" size="small" label="查询内容">
      <NInput
        v-model:value="crmInput"
        placeholder="请输入查询内容..."
        clearable
        round
        @keypress.enter="fetchCrmMax()"
      />
    </NFormItem>
    <NButton type="success" size="small" round @click="fetchCrmMax()">查询</NButton>
  </NSpace>
  <NDataTable :bordered="false" :columns="columns" :data="dataSource" :max-height="450" size="small" />
</template>

<style scoped></style>
