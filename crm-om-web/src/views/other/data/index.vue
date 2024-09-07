<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';

const B = ref<number>();
const KB = ref<number>();
const MB = ref<number>();
const GB = ref<number>();
const TB = ref<number>();
const dataValue = ref<string>('1');
const option = ref<number>(3);
const selectOptions = ref([
  {
    label: 'TB',
    value: 4
  },
  {
    label: 'GB',
    value: 3
  },
  {
    label: 'MB',
    value: 2
  },
  {
    label: 'KB',
    value: 1
  }
]);

function calculate() {
  const b = Number.parseInt(dataValue.value, 10) * 1024 ** option.value;
  B.value = b; // B
  KB.value = b / 1024; // KB
  MB.value = b / 1024 / 1024; // MB
  GB.value = b / 1024 / 1024 / 1024; // GB
  TB.value = b / 1024 / 1024 / 1024 / 1024; // TB
}

onMounted(() => {
  calculate();
});

// 单位变化监听
watch(option, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    calculate();
  }
});
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard title="流量换算" size="small" :bordered="false" class="card-wrapper sm:h-full" hoverable>
      <NGrid responsive="screen" item-responsive>
        <NFormItemGi show-require-mark span="24" label-placement="left" label="流量数值" path="type">
          <NSpace class="w-full">
            <NInput
              v-model:value="dataValue"
              size="small"
              placeholder="请输入..."
              round
              clearable
              @keypress.enter="calculate"
            />
            <NSelect v-model:value="option" size="small" class="w-25" :options="selectOptions" />
            <NButton type="success" size="small" round @click="calculate">换算</NButton>
          </NSpace>
        </NFormItemGi>
      </NGrid>
      <NDivider title-placement="left" dashed>换算结果</NDivider>
      <div class="indent-2em">
        <span class="text=[#2080f0] text-18px">{{ B }}</span>
        <span class="select-none pl-10px text-20px font-bold">B</span>
      </div>
      <div class="indent-2em">
        <span class="text=[#2080f0] text-18px">{{ KB }}</span>
        <span class="select-none pl-10px text-20px font-bold">KB</span>
      </div>
      <div class="indent-2em">
        <span class="text=[#2080f0] text-18px">{{ MB }}</span>
        <span class="select-none pl-10px text-20px font-bold">MB</span>
      </div>
      <div class="indent-2em">
        <span class="text=[#2080f0] text-18px">{{ GB }}</span>
        <span class="select-none pl-10px text-20px font-bold">GB</span>
      </div>
      <div class="indent-2em">
        <span class="text=[#2080f0] text-18px">{{ TB }}</span>
        <span class="select-none pl-10px text-20px font-bold">TB</span>
      </div>
      <NDivider title-placement="left" dashed>说明</NDivider>
      <div class="indent-2em">
        手机流量（网络流量）的基本单位是字节（B），更大的单位还有千字节（KB）、兆字节（MB）、吉字节（GB）等。在描述流量包时通常使用MB、GB为单位，运营商账单中也会给出KB为单位的流量数值以便精确计算。
      </div>
      <div class="indent-2em">手机流量以二进制计算，单位之间的进率是2的10次方为1024，也就是：</div>
      <div class="mt-1.25 rounded-1.25 bg-[#ecf4fa] p-1.25 dark:bg-[#303033] dark:text-#fff">
        <NCode code="1TB = 1024GB&#10;1GB = 1024MB&#10;1MB = 1024KB&#10;1KB = 1024B" word-wrap language="text" />
      </div>
    </NCard>
  </div>
</template>
