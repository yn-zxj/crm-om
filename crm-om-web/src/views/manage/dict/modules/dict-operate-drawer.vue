<script setup lang="ts">
import { reactive, watch } from 'vue';
import { $t } from '@/locales';
import { request } from '@/service/request';

defineOptions({
  name: 'DictOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.DictType | null;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', { default: false });

type Model = Pick<Api.SystemManage.DictType, 'dictType'>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    dictType: ''
  };
}

async function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
  }

  console.log('%s', JSON.stringify(await fetchDataList(model)));
}

/**
 * 获取字典数据
 *
 * @param params 字典类型
 */
function fetchDataList(params?: Api.SystemManage.DictDataSearchParams) {
  return request<Api.SystemManage.DictDataList>({
    url: '/dict/data',
    method: 'get',
    params
  });
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800">
    <NDrawerContent :title="$t('page.manage.dict.dictData')" :native-scrollbar="false" closable></NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
