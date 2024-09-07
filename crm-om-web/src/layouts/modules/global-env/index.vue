<script setup lang="ts">
import { ref } from 'vue';
import { localStg } from '@/utils/storage';
import { useSvgIcon } from '@/hooks/common/icon';

defineOptions({
  name: 'GlobalEnv'
});

const { SvgIconVNode } = useSvgIcon();

const envOptions = [
  {
    label: 'BSS',
    key: 'bss',
    icon: SvgIconVNode({ icon: 'tabler:square-rounded-letter-b' }),
    children: [
      {
        label: '测试',
        key: 'bss-test',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-t' })
      },
      {
        label: '生产',
        key: 'bss-prod',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-p' })
      }
    ]
  },
  {
    label: 'MVNE',
    key: 'mvne',
    icon: SvgIconVNode({ icon: 'tabler:square-rounded-letter-e' }),
    children: [
      {
        label: '测试',
        key: 'mvne-test',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-t' })
      },
      {
        label: '生产',
        key: 'mvne-prod',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-p' })
      }
    ]
  },
  {
    label: 'MVNO',
    key: 'mvno',
    icon: SvgIconVNode({ icon: 'tabler:square-rounded-letter-o' }),
    children: [
      {
        label: '测试',
        key: 'mvno-test',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-t' })
      },
      {
        label: '生产',
        key: 'mvno-prod',
        icon: SvgIconVNode({ icon: 'tabler:circle-letter-p' })
      }
    ]
  }
];

const mode = ref(`${localStg.get('platform') || 'bss'}-${localStg.get('env') || 'test'}`);

const envSelect = (key: string) => {
  mode.value = key;
  localStg.set('platform', key.split('-')[0]);
  localStg.set('env', key.split('-')[1]);
};
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
