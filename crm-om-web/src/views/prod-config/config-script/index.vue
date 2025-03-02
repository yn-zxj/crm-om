<!-- 产品配置脚本查询 -->
<script setup lang="ts">
import { ref } from 'vue';
import { NButton, useMessage } from 'naive-ui';
import { request } from '@/service/request';
import { downloadFile, handleCopy } from '@/utils/common';
import { localStg } from '@/utils/storage';

const message = useMessage();

const orderCardShow = ref(false);
const emptyTip = ref(true);
const qryLoading = ref(false);
const ref_prc = ref<string>('');

const prcScript = ref({
  out: '',
  fileName: ''
});

// 配置脚本查询
async function fetchPrcInfo() {
  emptyTip.value = false;
  qryLoading.value = true;

  const result = await request({
    url: '/prodConfig/configScript',
    method: 'post',
    timeout: 30000, // 30s 超时
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      prcId: ref_prc.value.trim().split(',')
    }
  });

  if (result?.data) {
    orderCardShow.value = true;
    prcScript.value.out = result.data;
    prcScript.value.fileName = `${ref_prc.value.replace(',', '-')}-总执行_回滚脚本.sql`;
  } else {
    orderCardShow.value = false;
    emptyTip.value = true;
    message.error('产品脚本获取失败!');
  }
  qryLoading.value = false;
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard :bordered="false" size="small" class="card-wrapper" hoverable>
      <NCollapse :default-expanded-names="['prod-search']">
        <NCollapseItem :title="$t('common.search')" name="prod-search">
          <NForm label-placement="left" :label-width="80">
            <NGrid responsive="screen" item-responsive>
              <!-- 查询条件 -->
              <NFormItemGi show-require-mark span="24 s:12 m:12" size="small" label="资费ID" class="pr-24px">
                <NInput
                  v-model:value="ref_prc"
                  placeholder="支持批量查询，如：AA,BB,CC..."
                  clearable
                  round
                  @keydown.enter="fetchPrcInfo"
                />
              </NFormItemGi>
              <NFormItemGi span="24 m:4" class="pr-24px">
                <NSpace class="w-full" justify="end">
                  <NButton type="success" round size="small" @click="fetchPrcInfo">
                    <template #icon>
                      <icon-ic-round-search class="text-icon" />
                    </template>
                    {{ $t('common.search') }}
                  </NButton>
                </NSpace>
              </NFormItemGi>
            </NGrid>
          </NForm>
        </NCollapseItem>
      </NCollapse>
    </NCard>
    <NCard title="配置脚本" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
      <NSpin :show="qryLoading">
        <div v-show="orderCardShow">
          <div
            class="mt-2.5 max-h-125 min-h-55 overflow-scroll rounded-1.25 bg-[#ecf4fa] p-1.25 dark:bg-[#303033] dark:text-#fff"
          >
            <NButtonGroup size="small" class="absolute right-12.5">
              <NButton secondary round @click="downloadFile(prcScript.out, prcScript.fileName)">
                <template #icon>
                  <SvgIcon icon="tabler:download" />
                </template>
              </NButton>
              <NButton secondary round @click="handleCopy(prcScript.out)">
                <template #icon>
                  <SvgIcon icon="uil:clipboard-notes" />
                </template>
              </NButton>
            </NButtonGroup>
            <NCode :code="prcScript.out" language="sql" />
          </div>
        </div>
        <div v-show="emptyTip">
          <NEmpty description="无数据"></NEmpty>
        </div>

        <template #description>正在获取产品脚本,请稍等...</template>
      </NSpin>
    </NCard>
  </div>
</template>
