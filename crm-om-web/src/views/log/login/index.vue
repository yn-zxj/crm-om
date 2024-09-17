<script setup lang="tsx">
import { NButton, NPopconfirm, NTag } from 'naive-ui';
import { useAppStore } from '@/store/modules/app';
import { useTable, useTableOperate } from '@/hooks/common/table';
import { $t } from '@/locales';
import { executeStatusRecord } from '@/constants/business';
import { request } from '@/service/request';
import LoginSearch from './modules/login-search.vue';

const appStore = useAppStore();

function fetchLogList(params?: Api.SystemManage.LoginSearchParams) {
  return request<Api.SystemManage.LoginList>({
    url: '/login/all',
    method: 'get',
    params
  });
}

/** 日志删除 */
function deleteLog(params: Array<string>) {
  return request<Api.SystemManage.LoginList>({
    url: `/login/del/${params}`,
    method: 'delete'
  });
}

const { columns, columnChecks, data, loading, getData, mobilePagination, searchParams, resetSearchParams } = useTable({
  apiFn: fetchLogList,
  apiParams: {
    current: 1,
    size: 10,
    status: null,
    infoId: null,
    userId: null
  },
  columns: () => [
    {
      type: 'selection',
      align: 'center',
      width: 48
    },
    {
      key: 'infoId',
      title: $t('page.manage.login.infoId'),
      align: 'center'
    },
    {
      key: 'userId',
      title: $t('page.manage.login.userId'),
      align: 'center'
    },
    {
      key: 'ip',
      title: $t('page.manage.login.ip'),
      align: 'center'
    },
    {
      key: 'msg',
      title: $t('page.manage.login.msg')
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
      key: 'accessTime',
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
          <NPopconfirm onPositiveClick={() => handleDelete(row.infoId)}>
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

const { checkedRowKeys, onBatchDeleted, onDeleted } = useTableOperate(data, getData);

async function handleBatchDelete() {
  deleteLog(checkedRowKeys.value);
  await onBatchDeleted();
}

async function handleDelete(id: string) {
  deleteLog([id]);
  await onDeleted();
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <LoginSearch v-model:model="searchParams" @reset="resetSearchParams" @search="getData" />
    <NCard :title="$t('page.manage.log.cardName')" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper">
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
        :row-key="row => row.infoId"
        :pagination="mobilePagination"
        class="sm:h-full"
      />
    </NCard>
  </div>
</template>

<style scoped></style>
