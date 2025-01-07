<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { useMagicKeys, whenever } from '@vueuse/core';
import { localStg } from '@/utils/storage';
import { useSvgIcon } from '@/hooks/common/icon';
import { request } from '@/service/request';

defineOptions({
  name: 'GlobalEnv'
});

const envOptions = ref<[{ label: string; key: string; icon?: string; disabled?: boolean }]>();

const mode = ref(`${localStg.get('platform') || 'bss'}-${localStg.get('env') || 'test'}`);

const keys = useMagicKeys();

/**
 * 查询平台环境信息
 *
 * @param params
 */
async function fetchEnv(params?: Api.SystemManage.DictTypeSearchParams) {
  return request<Api.SystemManage.DictDataList>({
    url: '/dict/data',
    method: 'get',
    params
  });
}

/**
 * 环境按键复制
 *
 * @param platform 平台
 * @param env 环境
 */
const handleSwitchEnv = (platform: string, env: string) => {
  localStg.set('platform', platform);
  localStg.set('env', env);
  mode.value = `${platform}-${env}`;
};

/** 快捷键配置 */
const keyBindings = [
  { key: 'Shift+KeyB+KeyT', platform: 'bss', env: 'test' },
  { key: 'Shift+KeyB+KeyP', platform: 'bss', env: 'prod' },
  { key: 'Shift+KeyE+KeyT', platform: 'mvne', env: 'test' },
  { key: 'Shift+KeyE+KeyP', platform: 'mvne', env: 'prod' },
  { key: 'Shift+KeyO+KeyT', platform: 'mvno', env: 'test' },
  { key: 'Shift+KeyO+KeyP', platform: 'mvno', env: 'prod' }
];

/** 按键绑定 */
keyBindings.forEach(({ key, platform, env }) => {
  whenever(keys[key]!, () => handleSwitchEnv(platform, env));
});

const { SvgIconVNode } = useSvgIcon();

const envSelect = (key: string) => {
  mode.value = key;
  localStg.set('platform', key.split('-')[0]);
  localStg.set('env', key.split('-')[1]);
};

onMounted(async () => {
  const envInfo = await fetchEnv({ dictType: 'sys_platform_env' });
  envOptions.value = envInfo.data.map(item => ({
    label: item.dictValue.label,
    key: item.dictValue.key,
    icon: SvgIconVNode(item.dictValue.icon),
    children: item.dictValue.children.map(item => ({
      label: item.label,
      key: item.key,
      icon: SvgIconVNode(item.icon)
    }))
  }));
  console.log(envInfo);
  console.log(envOptions.value);
});
</script>

<template>
  <NDropdown :value="mode" :show-arrow="true" size="small" :options="envOptions" trigger="click" @select="envSelect">
    <div>
      <ButtonIcon :tooltip-content="$t('common.env')">
        <SvgIcon v-if="mode === 'bss-test'" local-icon="Bt" />
        <SvgIcon v-if="mode === 'bss-prod'" local-icon="Bp" />
        <SvgIcon v-if="mode === 'mvne-test'" local-icon="Et" />
        <SvgIcon v-if="mode === 'mvne-prod'" local-icon="Ep" />
        <SvgIcon v-if="mode === 'mvno-test'" local-icon="Ot" />
        <SvgIcon v-if="mode === 'mvno-prod'" local-icon="Op" />
      </ButtonIcon>
    </div>
  </NDropdown>
</template>

<style scoped></style>
