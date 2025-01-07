<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import type { PaginationProps } from 'naive-ui';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';
import { request } from '@/service/request';
import { localStg } from '@/utils/storage';

const dataSource = ref();
const baseInput = ref('PCB');
const baseTab = ref('test');

const columns: TableColumn<any>[] = [
  {
    title: '编码',
    key: 'CODE',
    align: 'center'
  },
  {
    title: '语言',
    key: 'LANGUAGE',
    align: 'center'
  },
  {
    title: '值',
    key: 'VALUE',
    align: 'left'
  },
  {
    title: '键',
    key: 'KEY',
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

// 获取指定前缀的基础域前100条数据
async function fetchBaseMax(codePrefix: string) {
  const out = await request({
    url: `/prodConfig/baseInfo?code=${codePrefix}&platform=${localStg.get('platform')}&env=${baseTab.value}`,
    method: 'get'
  });
  if (out) {
    dataSource.value = out.data;
  } else {
    dataSource.value = [];
  }
}

// 监听tab切换
watch(baseTab, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pagination.page = 1; // 页码置1
    fetchBaseMax(baseInput.value);
  }
});

onMounted(() => {
  fetchBaseMax(baseInput.value);
});
</script>

<template>
  <!-- 查询条件 -->
  <NSpace>
    <NFormItem show-require-mark label-width="auto" label-placement="left" size="small" label="国际化编码">
      <NInput
        v-model:value="baseInput"
        placeholder="请输入..."
        clearable
        round
        @keydown.enter="fetchBaseMax(baseInput)"
      />
    </NFormItem>
    <NButton type="success" size="small" round @click="fetchBaseMax(baseInput)">查询</NButton>
  </NSpace>

  <NTabs v-model:value="baseTab" type="line" class="h-full flex-col-stretch" pane-class="flex-1-hidden">
    <NTabPane key="test" name="test" tab="测试">
      <NDataTable :bordered="false" :columns="columns" :data="dataSource" :pagination="pagination" size="small" />
    </NTabPane>
    <NTabPane key="prod" name="prod" tab="生产">
      <NDataTable :bordered="false" :columns="columns" :data="dataSource" :pagination="pagination" size="small" />
    </NTabPane>
  </NTabs>
</template>

<style scoped></style>
