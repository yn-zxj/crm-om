<script setup lang="ts">
import { ref, watch } from 'vue';
import { handleCopy } from '@/utils/common';

const formatInput = ref<string>('');
const formatMode = ref<string>('1');
const formatOut = ref<string>('');

// 字符串格式处理
function formatDeal(value: string) {
  // 默认单引号格式
  let deal = value.replace(/'/g, "\\'");
  // 处理双引号格式
  if (formatMode.value === '2') {
    deal = value.replace(/"/g, '\\"');
  }
  // 处理换行
  formatOut.value = deal.replace(/\n/g, '\\n');
}

// 模式监听
watch(formatMode, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    formatDeal(formatInput.value);
  }
});

// 输入监听
watch(formatInput, (newVal, oldVal) => {
  if (newVal !== oldVal) {
    formatDeal(formatInput.value);
  }
});
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard title="字符串格式化" size="small" :bordered="false" class="card-wrapper sm:h-full" hoverable>
      <NTag round :bordered="false" type="success">输入</NTag>
      <NInput
        v-model:value="formatInput"
        class="mt-2.5"
        placeholder="请输入字符串文本..."
        type="textarea"
        size="small"
        round
        :autosize="{
          minRows: 8,
          maxRows: 8
        }"
      />
      <NDivider dashed></NDivider>
      <NTag round :bordered="false" type="info">输出</NTag>
      <NSpace justify="end">
        <NButton color="#34C759" size="tiny" round @click="formatMode = '1'">单引号</NButton>
        <NButton color="#44CEF6" size="tiny" round @click="formatMode = '2'">双引号</NButton>
        <NButton color="#4A4266" size="tiny" round @click="handleCopy(formatOut)">
          <template #icon>
            <SvgIcon icon="uil:clipboard-notes" />
          </template>
        </NButton>
      </NSpace>
      <div class="mt-2.5 rounded-1.25 bg-[#ecf4fa] dark:bg-[#303033] dark:text-#fff">
        <NLog class="pb-2 pl-2 pt-2" :log="formatOut" :rows="14" word-wrap />
      </div>
    </NCard>
  </div>
</template>
