<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue';
import type { PaginationProps } from 'naive-ui';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';
import { request } from '@/service/request';

const dataSource = ref();
const attrInput = ref('EUB00003');
const baseTab = ref('test');

const columns: TableColumn<any>[] = [
  {
    title: '属性编码',
    key: 'element_id',
    align: 'center'
  },
  {
    title: '属性值',
    key: 'element_value',
    align: 'center'
  },
  {
    title: '属性名称',
    key: 'element_value_desc',
    align: 'center'
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

// 资费属性
async function fetchAttrInfo(attr_id: string) {
  const params = {
    attr_id
  };

  const out = await request({ url: '/prod/attrRule', method: 'post', data: params });
  if (out) {
    dataSource.value = out;
  } else {
    dataSource.value = [];
  }
}

// 监听tab切换
watch(baseTab, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    pagination.page = 1; // 页码置1
    fetchAttrInfo(attrInput.value);
  }
});

onMounted(() => {
  fetchAttrInfo(attrInput.value);
});
</script>

<template>
  <!-- 查询条件 -->
  <NSpace>
    <NFormItem show-require-mark label-width="auto" label-placement="left" size="small" label="属性编码">
      <NInput
        v-model:value="attrInput"
        placeholder="请输入..."
        clearable
        round
        @keypress.enter="fetchAttrInfo(attrInput)"
      />
    </NFormItem>
    <NButton type="success" size="small" round @click="fetchAttrInfo(attrInput)">查询</NButton>
  </NSpace>

  <NTabs v-model:value="baseTab" type="line" class="h-full flex-col-stretch" pane-class="flex-1-hidden">
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
