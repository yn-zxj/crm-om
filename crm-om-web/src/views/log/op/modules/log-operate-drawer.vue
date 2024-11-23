<script setup lang="ts">
import { reactive, watch } from 'vue';
import { $t } from '@/locales';
import { str2JSON } from '@/utils/common';
import { executeStatusRecord } from '@/constants/business';
import { useThemeStore } from '@/store/modules/theme';

defineOptions({
  name: 'LogOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Log | null;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', { default: false });

const themeStore = useThemeStore();
const lightStyle = 'width: 100%; padding: 0.4rem; border-radius: 0.4rem; background-color: #ECF4FA;';
const darkStyle = 'width: 100%; padding: 0.4rem; border-radius: 0.4rem; background-color: #303033;';

type Model = Pick<
  Api.SystemManage.Log,
  | 'title'
  | 'opUrl'
  | 'costTime'
  | 'opTime'
  | 'method'
  | 'opParam'
  | 'opResult'
  | 'businessType'
  | 'requestMethod'
  | 'opType'
  | 'status'
>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    title: '',
    opUrl: '',
    costTime: '',
    opTime: '',
    method: '',
    opParam: '',
    opResult: '',
    businessType: '',
    requestMethod: '',
    opType: '',
    status: null
  };
}

function handleInitModel() {
  Object.assign(model, createDefaultModel());

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model, props.rowData);
  }
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
  }
});
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="800">
    <NDrawerContent :title="$t('page.manage.log.drawerName')" :native-scrollbar="false" closable>
      <NDescriptions label-placement="left" label-style="font-weight: bold">
        <NDescriptionsItem :label="$t('page.manage.log.title')">
          <NTag type="success" :bordered="false" size="small" round>
            {{ model.title }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.opUrl')">
          <NTag type="warning" :bordered="false" size="small" round>
            {{ model.opUrl }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.requestMethod')">
          <NTag type="info" :bordered="false" size="small" round>
            {{ model.requestMethod }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.status')">
          <NTag :type="model.status == '0' ? 'success' : 'error'" :bordered="false" size="small" round>
            {{ model.status !== null ? $t(executeStatusRecord[model.status]) : '' }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.costTime')">
          <NTag type="info" :bordered="false" size="small" round>
            {{ model.costTime }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.opTime')">
          <NTag type="warning" :bordered="false" size="small" round>
            {{ model.opTime }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.log.method')" :span="3">
          <NTag type="success" :bordered="false" size="small" round>
            {{ model.method }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem
          :content-style="themeStore.darkMode ? darkStyle : lightStyle"
          :label="$t('page.manage.log.opParam')"
          :span="3"
        >
          <NCode :code="str2JSON(model.opParam)" language="json" />
        </NDescriptionsItem>
        <NDescriptionsItem
          :content-style="themeStore.darkMode ? darkStyle : lightStyle"
          :label="$t('page.manage.log.opResult')"
          :span="3"
        >
          <NCode word-wrap :code="str2JSON(model.opResult)" language="json" />
        </NDescriptionsItem>
      </NDescriptions>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
