<script setup lang="tsx">
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import { request } from '@/service/request';
import DictOperateDrawer from './modules/dict-operate-drawer.vue';
import DictSearch from './modules/dict-search.vue';

const appStore = useAppStore();

function fetchDictList(params?: Api.SystemManage.DictTypeSearchParams) {
  return request<Api.SystemManage.DictTypeList>({
    url: '/dict/type',
    method: 'get',
    params
  });
}

/** 类型删除 */
function deleteLog(params: Array<string>) {
  return request<Api.SystemManage.LogList>({
    url: `/log/del/${params}`,
    method: 'delete'
  });
}

const { columns, columnChecks, data, loading, getData, mobilePagination, searchParams, resetSearchParams } = useTable({
  apiFn: fetchDictList,
  apiParams: {
    current: 1,
    size: 10,
    status: null,
    dictName: null,
    dictType: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'dictName',
      title: $t('page.manage.dict.dictName'),
      align: 'center'
    },
    {
      key: 'dictType',
      title: $t('page.manage.dict.dictType'),
      align: 'center'
    },
    {
      key: 'remark',
      title: $t('page.manage.config.remark'),
      align: 'left'
    },
    {
      key: 'status',
      title: $t('page.manage.config.status'),
      align: 'center',
      width: 100,
      render: row => {
        if (row.status === null) {
          return null;
        }

        const tagMap: Record<Api.Common.EnableStatus, NaiveUI.ThemeColor> = {
          0: 'error',
          1: 'success'
        };

        const label = $t(enableStatusRecord[row.status]);

        return (
          <NTag size="small" round type={tagMap[row.status]}>
            {label}
          </NTag>
        );
      }
    },
    {
      key: 'createBy',
      title: $t('page.manage.common.createBy'),
      align: 'center'
    },
    {
      key: 'createTime',
      title: $t('page.manage.common.createTime'),
      align: 'center'
    },
    {
      key: 'updateBy',
      title: $t('page.manage.common.updateBy'),
      align: 'center'
    },
    {
      key: 'updateTime',
      title: $t('page.manage.common.updateTime'),
      align: 'center'
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 130,
      render: row => (
        <div class="flex-center gap-8px">
          <NButton type="primary" round ghost size="tiny" onClick={() => edit(row.dictId)}>
            {$t('common.view')}
          </NButton>
          <NPopconfirm onPositiveClick={() => handleDelete(row.dictId)}>
            {{
              default: () => $t('common.confirmDelete'),
              trigger: () => (
                <NButton type="error" round ghost size="tiny">
                  {$t('common.delete')}
                </NButton>
              )
            }}
          </NPopconfirm>
        </div>
      )
    }
  ]
});

const { drawerVisible, operateType, editingData, handleEdit, checkedRowKeys, onBatchDeleted, onDeleted } =
  useTableOperate(data, getData);

async function handleBatchDelete() {
  deleteLog(checkedRowKeys.value);
  await onBatchDeleted();
}

async function handleDelete(id: string) {
  deleteLog([id]);
  await onDeleted();
}

function edit(id: string) {
  handleEdit(id, 'dictId');
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <DictSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getData" />
    <NCard
      :title="$t('page.manage.dict.dictType')"
      :bordered="false"
      size="small"
      class="sm:flex-1-hidden card-wrapper"
    >
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
          :hidden-add="true"
          :disabled-delete="checkedRowKeys.length === 0"
          :loading="loading"
          @delete="handleBatchDelete"
          @refresh="getData"
        />
      </template>
      <NDataTable
        v-model:checked-row-keys="checkedRowKeys"
        :columns="columns"
        :data="data"
        size="small"
        :flex-height="!appStore.isMobile"
        :scroll-x="1000"
        :loading="loading"
        remote
        :row-key="row => row.dictId"
        :pagination="mobilePagination"
        class="sm:h-full"
      />
      <DictOperateDrawer v-model:visible="drawerVisible" :operate-type="operateType" :row-data="editingData" />
    </NCard>
  </div>
</template>

<style scoped></style>
