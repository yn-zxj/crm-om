<!-- 订单查询 -->
<script setup lang="ts">
import { h, reactive, ref } from 'vue';
import type { PaginationProps, StepsProps } from 'naive-ui';
import { NButton, NDescriptions, NDescriptionsItem, NFormItemGi } from 'naive-ui';
import dayjs from 'dayjs';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';
import { trim } from 'lodash-es';
import { request } from '@/service/request';
import { localStg } from '@/utils/storage';

const formRef = ref(null);
const showLog = ref(false);
const showContent = ref(false);
const showModal = ref(false);
const orderData = ref();
const currentStatus = ref<StepsProps['status']>('error');

let orderList: any = null;

const pagination: PaginationProps = reactive({
  page: 1,
  pageCount: 1,
  pageSize: 10,
  itemCount: 0,
  size: 'small',
  prefix({ itemCount }: any) {
    return `总记录数: ${itemCount}`;
  }
});

// 订单表格标题
const orderColumns: TableColumn<any>[] = [
  {
    title: '订单编号',
    key: 'ORDER_ID',
    align: 'center'
  },
  {
    title: '子订单编号',
    key: 'SUB_ORDER_ID',
    align: 'center'
  },
  {
    title: '受理时间',
    key: 'HANDLE_TIME',
    align: 'center',
    render(record: { HANDLE_TIME: string }) {
      return dayjs(record.HANDLE_TIME).format('YYYY-MM-DD HH:mm:ss');
    }
  },
  {
    title: '受理人',
    key: 'LOGIN_NO',
    align: 'center'
  },
  {
    title: '用户信息',
    key: 'SERVICE_NO',
    align: 'center'
  },
  {
    title: '业务名称',
    key: 'SUB_ORDER_NAME',
    align: 'center'
  },
  {
    title: '订单流转状态',
    key: 'STATUS_DESC',
    align: 'center'
  },
  {
    title: '详情',
    key: 'DETAILS',
    align: 'center',
    render: (row: { SUB_ORDER_ID: string }) => {
      return h(
        NButton,
        {
          size: 'tiny',
          strong: true,
          round: true,
          secondary: true,
          type: 'primary',
          onClick: () => orderDetails(row)
        },
        { default: () => '查看' }
      );
    }
  },
  {
    type: 'expand',
    renderExpand: rowData => {
      return h(
        NDescriptions,
        {
          column: 4,
          'label-placement': 'left',
          size: 'small'
        },
        {
          default: () => [
            h(
              NDescriptionsItem,
              { label: '实缴费用' },
              { default: () => Number.parseFloat(rowData.CURR_PAY).toFixed(2) }
            ),
            h(
              NDescriptionsItem,
              { label: '应缴费用' },
              { default: () => Number.parseFloat(rowData.SHOULD_PAY).toFixed(2) }
            ),
            h(
              NDescriptionsItem,
              { label: '业务优惠' },
              { default: () => Number.parseFloat(rowData.PROD_FAV).toFixed(2) }
            ),
            h(
              NDescriptionsItem,
              { label: '权限优惠' },
              { default: () => Number.parseFloat(rowData.POWER_FAV).toFixed(2) }
            )
          ]
        }
      );
    }
  }
];

const orderForm = ref({
  type: null,
  opCode: null,
  loginNo: null,
  time: null
});

const orderJSON = ref({
  bizcont_key: null,
  type: null,
  create_time: null,
  bizcont_value: null
});

const orderStep = (orderInfo: { RUN_STEP_ID: string; ORDER_STEPT_INFO_LIST: any[] }) => {
  let step = 1;
  const currentStep = orderInfo.RUN_STEP_ID;
  for (const list of orderInfo.ORDER_STEPT_INFO_LIST) {
    if (currentStep === list.RUN_STEP_ID) {
      step = list.NO;
      break;
    }
  }
  // 如果数组长度和step相等,说明订单完成,步骤条修改为完成
  if (orderInfo.ORDER_STEPT_INFO_LIST.length === step) {
    currentStatus.value = 'finish';
  }
  return step;
};

function handlePageChange(currentPage: number) {
  pagination.page = currentPage;
  fetchInfo();
}

// 订单数据查询
async function fetchInfo() {
  const startTime = orderForm.value.time !== null ? dayjs(orderForm.value.time[0]).format('YYYYMMDDHHssmm') : '';
  const endTime = orderForm.value.time !== null ? dayjs(orderForm.value.time[1]).format('YYYYMMDDHHssmm') : '';

  const body = {
    CURR_LOGIN_NO: orderForm.value.loginNo,
    pageNum: pagination.page,
    pageSize: pagination.pageSize,
    ALL_OR_HIS: '0',
    SEARCH_NO: orderForm.value.type,
    LOGIN_NO: orderForm.value.loginNo,
    BEGIN_TIME: startTime,
    END_TIME: endTime,
    OP_CODE: orderForm.value.opCode
  };

  const result = await request({
    url: '/bssApi/common',
    method: 'post',
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      type: '1',
      api: 'com_sitech_crm_order_inter_IOrderQuerySvc_newQueryAllOrderInfoList',
      params: body
    }
  });

  orderData.value = result.data?.SUB_ORDER_LIST;
  // eslint-disable-next-line require-atomic-updates
  pagination.itemCount = result?.data.SUB_ORDER_NUM;
}

// 订单详情查询
async function orderDetails(info: { SUB_ORDER_ID: string }) {
  // 详情查询前隐藏
  showContent.value = false;
  showLog.value = false;

  const body = {
    ALL_OR_HIS: '0',
    SUB_ORDER_ID: info.SUB_ORDER_ID
  };

  const result = await request({
    url: '/bssApi/common',
    method: 'post',
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      type: '1',
      api: 'com_sitech_crm_order_inter_IOrderQuerySvc_queryOrderDetails',
      params: body
    }
  });

  showModal.value = true;
  orderList = result.data?.ORDER_LINE_LIST;
}

// 订单报文查询
async function fetchOrderJSON(orderLineId: string) {
  const result = await request({
    url: `/bssApi/orderInfo?platform=${localStg.get('platform')}&env=${localStg.get('env')}&orderLineId=${trim(orderLineId)}`,
    method: 'get'
  });

  if (result?.data) {
    orderJSON.value = result.data;
  }
  showContent.value = true;
}

// 表单重置
async function resetForm() {
  orderForm.value.type = null;
  orderForm.value.time = null;
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard size="small" :bordered="false" class="card-wrapper" hoverable>
      <!-- 查询条件 -->
      <NCollapse :default-expanded-names="['search']">
        <NCollapseItem :title="$t('common.search')" name="search">
          <NForm ref="formRef" label-placement="left" label-width="80" :model="orderForm" size="small">
            <NGrid responsive="screen" item-responsive>
              <NFormItemGi span="24 s:8 m:8" label="查询条件" path="type">
                <NInput
                  id="type"
                  v-model:value="orderForm.type"
                  placeholder="请输入..."
                  round
                  @keypress.enter="fetchInfo"
                />
              </NFormItemGi>
              <NFormItemGi span="24 s:10 m:12" label="时间" path="time">
                <NDatePicker v-model:value="orderForm.time" type="datetimerange" clearable default-time="00:00:00" />
              </NFormItemGi>
              <NFormItemGi span="24 m:4" class="pr-24px">
                <NSpace class="w-full" justify="end">
                  <NButton size="small" round @click="resetForm">
                    <template #icon>
                      <icon-ic-round-refresh class="text-icon" />
                    </template>
                    {{ $t('common.reset') }}
                  </NButton>
                  <NButton type="success" round size="small" @click="fetchInfo">
                    <template #icon>
                      <icon-ic-round-search class="text-icon" />
                    </template>
                    {{ $t('common.search') }}
                  </NButton>
                </NSpace>
              </NFormItemGi>
            </NGrid>
            <NBlockquote class="mb-10px ml-15px mr-15px mt--3.75">
              <NText depth="3">查询条件可以选择订单号、子订单号、订单行号、服务号</NText>
            </NBlockquote>
          </NForm>
        </NCollapseItem>
      </NCollapse>
    </NCard>

    <!-- 数据选项卡 -->
    <NCard title="订单列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
      <NTabs type="line" animated>
        <NTabPane name="allOrder" tab="全部订单">
          <NDataTable
            :columns="orderColumns"
            :data="orderData"
            :scroll-x="962"
            :pagination="pagination"
            :row-key="(row: any) => row.ORDER_ID"
            remote
            size="small"
            class="sm:h-full"
            @update:page="handlePageChange"
          />
        </NTabPane>
      </NTabs>

      <!-- 订单模态框 -->
      <NModal v-model:show="showModal" preset="card" class="w-70em" title="订单流转详情" size="huge" :bordered="false">
        <div v-for="(orderInfo, index) of orderList" :key="index">
          <NDescriptions label-placement="left" label-style="color: #aaa;" size="small">
            <NDescriptionsItem label="订单行编号">
              <NSpace>
                <NTag round :bordered="false" size="small" type="info">{{ orderInfo.ORDER_LINE_ID }}</NTag>
                <NButton
                  size="tiny"
                  type="success"
                  secondary
                  round
                  strong
                  @click="fetchOrderJSON(orderInfo.ORDER_LINE_ID)"
                >
                  报文
                </NButton>
              </NSpace>
            </NDescriptionsItem>
            <NDescriptionsItem label="用户信息">
              <NTag round :bordered="false" size="small" type="info">{{ orderInfo.SERVICE_NO }}</NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="业务详情">
              <NTag round :bordered="false" size="small" type="info">{{ orderInfo.OP_NAME }}</NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="业务优惠">
              <NTag round :bordered="false" size="small" type="success">
                {{ parseFloat(orderInfo.PROD_FAV).toFixed(2) }}
              </NTag>
            </NDescriptionsItem>

            <NDescriptionsItem label="权限优惠">
              <NTag round :bordered="false" size="small" type="success">
                {{ parseFloat(orderInfo.POWER_FAV).toFixed(2) }}
              </NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="应缴费用">
              <NTag round :bordered="false" size="small" type="success">
                {{ parseFloat(orderInfo.SHOULD_PAY).toFixed(2) }}
              </NTag>
            </NDescriptionsItem>

            <NDescriptionsItem label="实缴费用">
              <NTag round :bordered="false" size="small" type="success">
                {{ parseFloat(orderInfo.CURR_PAY).toFixed(2) }}
              </NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="流转步骤">
              <NTag round :bordered="false" size="small" type="info">{{ orderInfo.RUN_STEP_NAME }}</NTag>
            </NDescriptionsItem>
            <NDescriptionsItem label="订单流转状态">
              <NSpace>
                <NTag v-if="orderInfo.STATUS === 'C'" round :bordered="false" size="small" type="success">
                  {{ orderInfo.STATUS_DESC }}
                </NTag>
                <NTag v-if="orderInfo.STATUS !== 'C'" round :bordered="false" size="small" type="error">
                  {{ orderInfo.STATUS_DESC }}
                </NTag>
                <NButton
                  v-if="orderInfo.STATUS !== 'C'"
                  size="tiny"
                  type="error"
                  secondary
                  strong
                  round
                  @click="showLog = !showLog"
                >
                  详情
                </NButton>
              </NSpace>
            </NDescriptionsItem>
          </NDescriptions>
          <NDivider dashed />
          <!-- 订单流转步骤 -->
          <NSpace class="mt-7.5" justify="center">
            <NSteps :current="orderStep(orderInfo)" size="small" :status="currentStatus">
              <NStep
                v-for="(item, stepIndex) of orderInfo.ORDER_STEPT_INFO_LIST"
                :key="stepIndex"
                :title="item.RUN_STEP_NAME"
                :description="
                  item.OL_EXEC_TIME ? dayjs(item.OL_EXEC_TIME).format('YYYY-MM-DD HH:mm:ss') : item.STATUS_DESC
                "
              />
            </NSteps>
          </NSpace>
          <NDivider v-show="showLog" dashed title-placement="left">错误信息</NDivider>
          <NLog
            v-show="showLog"
            class="mt-2.5 border-rd-1.25 bg-[#ecf4fa] p-1.25"
            :log="orderInfo.ORDER_LINE_EXCP_MSG"
            :rows="10"
          />
          <NDivider v-show="showContent" dashed title-placement="left">订单报文</NDivider>
          <div
            v-show="showContent"
            class="mt-2.5 max-h-125 min-h-55 overflow-scroll rounded-1.25 bg-[#ecf4fa] p-1.25 dark:bg-[#303033] dark:text-#fff"
          >
            <NButtonGroup size="small" class="absolute right-12.5">
              <NButton secondary round @click="showContent = false">
                <template #icon>
                  <SvgIcon icon="zondicons:view-hide" />
                </template>
              </NButton>
            </NButtonGroup>
            <NCode :code="JSON.stringify(JSON.parse(orderJSON.bizcont_value as any), null, 2)" language="json" />
          </div>
        </div>
      </NModal>
    </NCard>
  </div>
</template>
