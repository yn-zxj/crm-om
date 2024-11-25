<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import type { PaginationProps } from 'naive-ui';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';

const dataSource = ref();
const ruleInput = ref('1');
const envTab = ref('test');

const columns: TableColumn<any>[] = [
  {
    title: '编码规则',
    key: 'rule_id',
    align: 'left'
  },
  {
    title: '规则名称',
    key: 'rule_name',
    align: 'left'
  },
  {
    title: '偏移单位',
    key: 'offset_unit',
    align: 'left'
  },
  {
    title: '偏移周期',
    key: 'offset_cycle',
    align: 'left'
  }
];

const pagination: PaginationProps = reactive({
  page: 1,
  pageSize: 10,
  showSizePicker: true,
  pageSizes: [10, 15, 20],
  size: 'small',
  onChange: (page: number) => {
    pagination.page = page;
  },
  onUpdatePageSize: (pageSize: number) => {
    pagination.page = 1;
    pagination.pageSize = pageSize;
  }
});

// 查询资费规则
async function fetchRuleInfo(offset: string) {}

// 监听tab切换
watch(envTab, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pagination.page = 1; // 页码置1
    fetchRuleInfo(ruleInput.value);
  }
});

onMounted(() => {
  fetchRuleInfo(ruleInput.value);
});
</script>

<template>
  <!-- 查询条件 -->
  <NSpace>
    <NFormItem show-require-mark label-width="auto" label-placement="left" size="small" label="偏移周期">
      <NInput
        v-model:value="ruleInput"
        placeholder="数字格式..."
        clearable
        round
        @keypress.enter="fetchRuleInfo(ruleInput)"
      />
    </NFormItem>
    <NButton type="success" size="small" round @click="fetchRuleInfo(ruleInput)">查询</NButton>
  </NSpace>

  <NTabs v-model:value="envTab" type="line" class="h-full flex-col-stretch" pane-class="flex-1-hidden">
    <NTabPane key="test" name="test" tab="测试">
      <NDataTable
        :bordered="false"
        :columns="columns"
        :data="dataSource"
        :pagination="pagination"
        :max-height="370"
        size="small"
      />
    </NTabPane>
    <NTabPane key="prod" name="prod" tab="生产">
      <NDataTable
        :bordered="false"
        :columns="columns"
        :data="dataSource"
        :pagination="pagination"
        :max-height="370"
        size="small"
      />
    </NTabPane>
  </NTabs>
</template>

<style scoped></style>
