<script setup lang="tsx">
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import { request } from '@/service/request';
import ConfigOperateDrawer from './modules/config-operate-drawer.vue';
import ConfigSearch from './modules/config-search.vue';

const appStore = useAppStore();

function fetchLogList(params?: Api.SystemManage.ConfigSearchParams) {
  return request<Api.SystemManage.ConfigList>({
    url: '/config/all',
    method: 'get',
    params
  });
}

/** 配置删除 */
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
    configName: null,
    configKey: null,
    configType: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'configName',
      title: $t('page.manage.config.configName'),
      align: 'left'
    },
    {
      key: 'configType',
      title: $t('page.manage.config.configType'),
      align: 'center'
    },
    {
      key: 'configKey',
      title: $t('page.manage.config.configKey'),
      align: 'left'
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
      key: 'operate',
      title: $t('common.operate'),
      align: 'center',
      width: 130,
      render: row => (
        <div class="flex-center gap-8px">
          <NButton type="primary" ghost size="tiny" onClick={() => edit(row.configId)}>
            {$t('common.view')}
          </NButton>
          <NPopconfirm onPositiveClick={() => handleDelete(row.configId)}>
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
  handleEdit(id, 'configId');
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <ConfigSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getData" />
    <NCard :title="$t('page.manage.config.title')" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
      <template #header-extra>
        <TableHeaderOperation
          v-model:columns="columnChecks"
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
      <ConfigOperateDrawer v-model:visible="drawerVisible" :operate-type="operateType" :row-data="editingData" />
    </NCard>
  </div>
</template>

<style scoped></style>
