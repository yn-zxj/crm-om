<script setup lang="tsx">
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { executeStatusRecord } from '@/constants/business';
import { request } from '@/service/request';
import LogOperateDrawer from './modules/log-operate-drawer.vue';
import LogSearch from './modules/log-search.vue';

const appStore = useAppStore();

function fetchLogList(params?: Api.SystemManage.LogSearchParams) {
  return request<Api.SystemManage.LogList>({
    url: '/log/all',
    method: 'get',
    params
  });
}

/** 日志删除 */
function deleteLog(params: Array<string>) {
  return request<Api.SystemManage.LogList>({
    url: `/log/del/${params}`,
    method: 'delete'
  });
}

const { columns, columnChecks, data, loading, getData, mobilePagination, searchParams, resetSearchParams } = useTable({
  apiFn: fetchLogList,
  apiParams: {
    current: 1,
    size: 10,
    status: null,
    businessType: null,
    requestMethod: null,
    opType: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'title',
      title: $t('page.manage.log.title'),
      align: 'center'
    },
    {
      key: 'opUrl',
      title: $t('page.manage.log.opUrl'),
      align: 'center'
    },
    {
      key: 'method',
      title: $t('page.manage.log.method'),
      width: 300
    },
    {
      key: 'requestMethod',
      title: $t('page.manage.log.requestMethod'),
      align: 'center',
      width: 150,
      render: row => {
        if (row.requestMethod === null) {
          return null;
        }

        const tagMap: Record<string, NaiveUI.ThemeColor> = {
          GET: 'success',
          POST: 'info',
          PUT: 'warning',
          DELETE: 'error'
        };

        return (
          <NTag size="small" round type={tagMap[row.requestMethod]}>
            {row.requestMethod}
          </NTag>
        );
      }
    },
    {
      key: 'status',
      title: $t('page.manage.log.status'),
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

        const label = $t(executeStatusRecord[row.status]);

        return (
          <NTag size="small" round type={tagMap[row.status]}>
            {label}
          </NTag>
        );
      }
    },
    {
      key: 'costTime',
      title: $t('page.manage.log.costTime'),
      align: 'center'
    },
    {
      key: 'opTime',
      title: $t('page.manage.log.opTime'),
      align: 'center'
    },
    {
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 130,
      render: row => (
        <div class="flex-center gap-8px">
          <NButton type="primary" ghost size="tiny" onClick={() => edit(row.opId)}>
            {$t('common.view')}
          </NButton>
          <NPopconfirm onPositiveClick={() => handleDelete(row.opId)}>
            {{
              default: () => $t('common.confirmDelete'),
              trigger: () => (
                <NButton type="error" ghost size="tiny">
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
  handleEdit(id, 'opId');
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <LogSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getData" />
    <NCard :title="$t('page.manage.log.cardName')" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
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
        :scroll-x="702"
        :loading="loading"
        remote
        :row-key="row => row.opId"
        :pagination="mobilePagination"
        class="sm:h-full"
      />
      <LogOperateDrawer v-model:visible="drawerVisible" :operate-type="operateType" :row-data="editingData" />
    </NCard>
  </div>
</template>

<style scoped></style>
