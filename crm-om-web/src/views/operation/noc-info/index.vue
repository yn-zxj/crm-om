<!-- 网元指令查询 -->
<script setup lang="ts">
import { ref } from 'vue';
import { NButton, useMessage } from 'naive-ui';
import { request } from '@/service/request';
import { downloadFile, handleCopy } from '@/utils/common';

const message = useMessage();
const orderCardShow = ref(false);
const imsi = ref<string>('');
const isdn = ref<string>('');
const option = ref<string>('pcrf');
const selectOptions = ref([
  {
    label: 'HSS',
    value: 'hss'
  },
  {
    label: 'PCRF',
    value: 'pcrf'
  },
  {
    label: 'PCRF_PSRV',
    value: 'pcrf_psrv'
  },
  {
    label: 'NP',
    value: 'np'
  },
  {
    label: 'SMSC',
    value: 'smsc'
  },
  {
    label: 'SACP',
    value: 'sacp'
  }
]);

const imsiRule = {
  trigger: ['input', 'blur'],
  validator: () => {
    if (!imsi.value) {
      return new Error('不能为空！');
    }
    return true;
  }
};

const nocInfo = ref('');

async function fetchNocInfo() {
  const inParam = {
    IMSI: imsi.value,
    ISDN: isdn.value,
    region_code: option.value
  };

  const result = await request({
    url: '/bssApi/nocInfo',
    method: 'post',
    data: {
      env: 'prod',
      platform: 'bss',
      type: '4',
      api: '/',
      params: inParam
    }
  });
  if (result?.data) {
    orderCardShow.value = true;
    nocInfo.value = result?.data as string;
  } else {
    message.error('网元指令查询出错!');
    orderCardShow.value = false;
  }
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard title="网元指令查询" :bordered="false" size="small" class="card-wrapper" hoverable>
      <NForm label-placement="left" :label-width="80">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi show-require-mark span="24 s:12 m:5" size="small" label="网元" class="pr-24px">
            <NSelect v-model:value="option" :options="selectOptions" />
          </NFormItemGi>
          <NFormItemGi
            v-if="option !== 'smsc' && option !== 'sacp'"
            show-require-mark
            span="24 s:12 m:12"
            size="small"
            label="IMSI"
            :rule="imsiRule"
            class="pr-24px"
          >
            <NInput v-model:value="imsi" placeholder="请输入..." round clearable @keypress.enter="fetchNocInfo" />
          </NFormItemGi>

          <NFormItemGi
            v-if="option === 'smsc' || option === 'sacp'"
            show-require-mark
            span="24 s:12 m:6"
            size="small"
            label="香港号码"
            :rule="imsiRule"
            class="pr-24px"
          >
            <NInput v-model:value="isdn" placeholder="请输入..." round clearable @keypress.enter="fetchNocInfo" />
          </NFormItemGi>
          <NFormItemGi
            v-if="option === 'smsc'"
            show-require-mark
            span="24 s:12 m:6"
            size="small"
            label="内地号码"
            :rule="imsiRule"
            class="pr-24px"
          >
            <NInput v-model:value="imsi" placeholder="请输入..." round clearable @keypress.enter="fetchNocInfo" />
          </NFormItemGi>

          <NFormItemGi span="24 m:4" class="pr-24px">
            <NSpace class="w-full" justify="end">
              <NButton type="success" round size="small" @click="fetchNocInfo">
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
    <NCard title="返回信息" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
      <div v-show="orderCardShow">
        <div
          class="mt-2.5 max-h-125 min-h-55 overflow-scroll rounded-1.25 bg-[#ecf4fa] p-1.25 dark:bg-[#303033] dark:text-#fff"
        >
          <NButtonGroup size="small" class="absolute right-12.5">
            <NButton round secondary @click="downloadFile(nocInfo, imsi + '_' + option + '.txt')">
              <template #icon>
                <SvgIcon icon="tabler:download" />
              </template>
            </NButton>
            <NButton secondary round @click="handleCopy(nocInfo)">
              <template #icon>
                <SvgIcon icon="uil:clipboard-notes" />
              </template>
            </NButton>
          </NButtonGroup>
          <NCode :code="nocInfo" />
        </div>
      </div>
      <div v-show="!orderCardShow">
        <NEmpty description="无数据"></NEmpty>
      </div>
    </NCard>
  </div>
</template>
