<script setup lang="ts">
import { ref } from 'vue';
import { NButton, NFormItemGi } from 'naive-ui';
import { trim } from 'lodash-es';
import dayjs from 'dayjs';
import { localStg } from '@/utils/storage';
import { request } from '@/service/request';
import { downloadFile, handleCopy } from '@/utils/common';

const orderCardShow = ref(false);
const orderLineId = ref<string>('');

const orderJSON = ref({
  bizcont_key: '',
  type: '',
  create_time: '',
  bizcont_value: ''
});

// 订单报文查询
async function fetchOrderJSON() {
  const result = await request({
    url: `/bssApi/orderInfo?platform=${localStg.get('platform')}&env=${localStg.get('env')}&orderLineId=${trim(orderLineId.value)}`,
    method: 'get'
  });

  if (result?.data) {
    orderJSON.value = result.data;
    orderCardShow.value = true;
  } else {
    orderCardShow.value = false;
  }
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard size="small" :bordered="false" class="card-wrapper" hoverable>
      <NCollapse :default-expanded-names="['search']">
        <NCollapseItem :title="$t('common.search')" name="search">
          <NForm label-placement="left" label-width="80" size="small">
            <NGrid responsive="screen" item-responsive>
              <NFormItemGi span="24 s:8 m:8" label="查询条件" path="type">
                <NInput
                  id="type"
                  v-model:value="orderLineId"
                  placeholder="请输入..."
                  round
                  @keydown.enter="fetchOrderJSON"
                />
              </NFormItemGi>

              <NFormItemGi span="24 m:4" class="pr-24px">
                <NSpace class="w-full" justify="end">
                  <NButton type="success" round size="small" @click="fetchOrderJSON">
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

    <!-- 数据选项卡 -->
    <NCard title="订单报文" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
      <div v-show="orderCardShow">
        <NDescriptions label-placement="left" label-style="color: #aaa;" size="small">
          <NDescriptionsItem label="报文编码">
            <NTag round :bordered="false" size="small" type="info">
              {{ orderJSON.bizcont_key }}
            </NTag>
          </NDescriptionsItem>
          <NDescriptionsItem label="订单类型">
            <NTag v-if="orderJSON.type === 'A'" round :bordered="false" size="small" type="success">
              正常
              <template #icon>
                <SvgIcon icon="carbon:checkmark-filled" />
              </template>
            </NTag>
            <NTag v-if="orderJSON.type === 'E'" round :bordered="false" size="small" type="error">
              异常
              <template #icon>
                <SvgIcon icon="ic:sharp-error" />
              </template>
            </NTag>
          </NDescriptionsItem>
          <NDescriptionsItem label="创建时间">
            <NTag round :bordered="false" size="small" type="info">
              {{ dayjs(orderJSON.create_time).format('YYYY-MM-DD HH:mm:ss') }}
            </NTag>
          </NDescriptionsItem>
        </NDescriptions>
        <div
          class="mt-2.5 max-h-125 min-h-55 overflow-scroll rounded-1.25 bg-[#ecf4fa] p-1.25 dark:bg-[#303033] dark:text-#fff"
        >
          <NButtonGroup size="small" class="absolute right-12.5">
            <NButton secondary round @click="downloadFile(orderJSON.bizcont_value, orderLineId + '.json')">
              <template #icon>
                <SvgIcon icon="tabler:download" />
              </template>
            </NButton>
            <NButton secondary round @click="handleCopy(orderJSON.bizcont_value)">
              <template #icon>
                <SvgIcon icon="uil:clipboard-notes" />
              </template>
            </NButton>
          </NButtonGroup>
          <NCode :code="orderJSON.bizcont_value" language="json" />
        </div>
      </div>
      <div v-show="!orderCardShow">
        <NEmpty description="无数据"></NEmpty>
      </div>
    </NCard>
  </div>
</template>
