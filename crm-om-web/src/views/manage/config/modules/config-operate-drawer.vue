<script setup lang="ts">
import { reactive, watch } from 'vue';
import { $t } from '@/locales';
import { enableStatusRecord } from '@/constants/business';
import { useThemeStore } from '@/store/modules/theme';

defineOptions({
  name: 'ConfigOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Config | null;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', { default: false });

const themeStore = useThemeStore();
const lightStyle = 'padding:0 0.3125rem 0 0.3125rem;border-radius: 0.3125rem;background-color: rgb(236 244 250);';
const darkStyle =
  'padding:0 0.3125rem 0 0.3125rem;border-radius: 0.3125rem;background-color: rgb(236 244 250);background-color: rgb(48 48 51);';

type Model = Pick<
  Api.SystemManage.Config,
  | 'configId'
  | 'configName'
  | 'configKey'
  | 'configType'
  | 'configValue'
  | 'remark'
  | 'status'
  | 'createBy'
  | 'createTime'
  | 'updateBy'
  | 'updateTime'
>;

const model: Model = reactive(createDefaultModel());

function createDefaultModel(): Model {
  return {
    configId: '',
    configName: '',
    configKey: '',
    configValue: '',
    configType: '',
    remark: '',
    status: null,
    createBy: '',
    createTime: '',
    updateBy: '',
    updateTime: ''
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
        <NDescriptionsItem :label="$t('page.manage.config.configId')">
          <NTag type="success" :bordered="false" size="small" round>
            {{ model.configId }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.config.configName')">
          <NTag type="warning" :bordered="false" size="small" round>
            {{ model.configName }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.config.configKey')">
          <NTag type="info" :bordered="false" size="small" round>
            {{ model.configKey }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.config.status')">
          <NTag :type="model.status == '0' ? 'success' : 'error'" :bordered="false" size="small" round>
            {{ model.status !== null ? $t(enableStatusRecord[model.status]) : '' }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem :label="$t('page.manage.config.remark')" :span="3">
          <NTag type="success" :bordered="false" size="small" round>
            {{ model.remark }}
          </NTag>
        </NDescriptionsItem>
        <NDescriptionsItem
          :content-style="themeStore.darkMode ? darkStyle : lightStyle"
          :label="$t('page.manage.config.configValue')"
          :span="3"
        >
          <NCode word-wrap :code="JSON.stringify(model.configValue, null, 2)" />
        </NDescriptionsItem>
      </NDescriptions>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
