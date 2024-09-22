<script setup lang="ts">
import { $t } from '@/locales';
import { executeStatusOptions, requestMethodOptions } from '@/constants/business';
import { translateOptions } from '@/utils/common';

defineOptions({
  name: 'ConfigSearch'
});

interface Emits {
  (e: 'reset'): void;
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const model = defineModel<Api.SystemManage.ConfigSearchParams>('model', { required: true });

function reset() {
  emit('reset');
}

function search() {
  emit('search');
}
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse :default-expanded-names="['oplog-search']">
      <NCollapseItem :title="$t('common.search')" name="oplog-search">
        <NForm :model="model" label-placement="left" :label-width="80" size="small">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:4 m:6" :label="$t('page.manage.config.configKey')" path="configKey" class="pr-24px">
              <NInput v-model:value="model.configKey" round />
            </NFormItemGi>
            <NFormItemGi
              span="24 s:4 m:5"
              :label="$t('page.manage.config.configName')"
              path="configName"
              class="pr-24px"
            >
              <NInput v-model:value="model.configName" round />
            </NFormItemGi>
            <NFormItemGi
              span="24 s:4 m:4"
              :label="$t('page.manage.config.configType')"
              path="configType"
              class="pr-24px"
            >
              <NSelect v-model:value="model.configType" :options="requestMethodOptions" clearable />
            </NFormItemGi>
            <NFormItemGi span="24 s:4 m:5" :label="$t('page.manage.config.status')" path="status" class="pr-24px">
              <NSelect v-model:value="model.status" :options="translateOptions(executeStatusOptions)" clearable />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:4">
              <NSpace class="w-full" justify="end">
                <NButton size="small" round @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="text-icon" />
                  </template>
                  {{ $t('common.reset') }}
                </NButton>
                <NButton size="small" round type="primary" ghost @click="search">
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
</template>

<style scoped></style>
