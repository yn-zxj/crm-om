<!-- 产品配置脚本查询 -->
<script setup lang="ts">
import { ref } from 'vue';
import { NButton, useMessage } from 'naive-ui';
import { request } from '@/service/request';
import { downloadFile, handleCopy } from '@/utils/common';
import { localStg } from '@/utils/storage';

const message = useMessage();

const orderCardShow = ref(false);
const ref_prc = ref<string>('');

const prcScript = ref({
  out: '',
  fileName: ''
});

// 配置脚本查询
async function fetchPrcInfo() {
  message.loading('正在获取产品脚本,请稍等...', { duration: 30000 });

  const result = await request({
    url: '/prodConfig/configScript',
    method: 'post',
    timeout: 30000, // 30s 超时
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      prcId: ref_prc.value.split(',')
    }
  });

  if (result?.data) {
    // 数据查询结束,销户提示
    message.destroyAll();

    orderCardShow.value = true;
    prcScript.value.out = result.data;
    prcScript.value.fileName = `${ref_prc.value.replace(',', '-')}-总执行_回滚脚本.sql`;
  } else {
    orderCardShow.value = false;
    message.error('产品脚本获取失败!');
  }
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard title="产品配置脚本" :bordered="false" size="small" class="card-wrapper" hoverable>
      <NForm label-placement="left" :label-width="80">
        <NGrid responsive="screen" item-responsive>
          <!-- 查询条件 -->
          <NFormItemGi show-require-mark span="24 s:12 m:12" size="small" label="资费ID" class="pr-24px">
            <NInput
              v-model:value="ref_prc"
              placeholder="支持批量查询，如：AA,BB,CC..."
              clearable
              round
              @keypress.enter="fetchPrcInfo"
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
    </NCard>
    <NCard title="配置脚本" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
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
      <div v-show="!orderCardShow">
        <NEmpty description="无数据"></NEmpty>
      </div>
    </NCard>
  </div>
</template>
