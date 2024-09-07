<script setup lang="ts">
import { h, ref } from 'vue';
import { NButton, NLog } from 'naive-ui';
import type { TableColumn } from 'naive-ui/es/data-table/src/interface';
import dayjs from 'dayjs';
import { request } from '@/service/request';
import { localStg } from '@/utils/storage';

const formRef = ref(null);
const orderData = ref();

// 订单表格标题
const orderColumns: TableColumn<any>[] = [
  {
    title: '订单编号',
    key: 'ORDER_ID',
    align: 'center'
  },
  {
    title: '订单行编号',
    key: 'ORDER_LINE_ID',
    align: 'center'
  },
  {
    title: '服务号码',
    key: 'SERVICE_NO',
    align: 'center'
  },
  {
    title: '业务名称',
    key: 'OP_NAME',
    align: 'center'
  },
  {
    title: '环节名称',
    key: 'RUN_STEP_NAME',
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
    title: '已交金额',
    key: 'CURR_PAY',
    align: 'center',
    render(record: { CURR_PAY: number }) {
      return (record.CURR_PAY / 1000).toFixed(2);
    }
  },
  {
    title: '流转状态',
    key: 'STATUS_DESC',
    align: 'center'
  },
  {
    title: '操作',
    key: 'DETAILS',
    align: 'center',
    render(row: { ORDER_LINE_ID: string; STATUS: string }) {
      return [
        h(
          NButton,
          {
            size: 'small',
            round: true,
            secondary: true,
            type: 'primary',
            disabled: row.STATUS === 'C',
            onClick: () => handleOrder(row.ORDER_LINE_ID, 2)
          },
          { default: () => '重发' }
        ),
        h(
          NButton,
          {
            style: {
              marginLeft: '6px'
            },
            size: 'small',
            round: true,
            secondary: true,
            type: 'warning',
            disabled: row.STATUS === 'C',
            onClick: () => handleOrder(row.ORDER_LINE_ID, 1)
          },
          { default: () => '撤单' }
        )
      ];
    }
  },
  {
    type: 'expand',
    expandable: (rowData: { STATUS: string }) => rowData.STATUS !== 'C',
    renderExpand: (rowData: { DETAIL_MSG: string; ORDER_LINE_ID: string }) => {
      return h(
        NLog,
        {
          log: rowData.DETAIL_MSG,
          'font-size': 10,
          rows: 20
        },
        {}
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

// 订单数据查询
async function fetchInfo() {
  const startTime = orderForm.value.time !== null ? dayjs(orderForm.value.time[0]).format('YYYYMMDDHHssmm') : '';
  const endTime = orderForm.value.time !== null ? dayjs(orderForm.value.time[1]).format('YYYYMMDDHHssmm') : '';

  const body = {
    INPUT_VALUE: orderForm.value.type !== null ? orderForm.value.type : '',
    HANDLE_TIME_START: startTime,
    HANDLE_TIME_END: endTime,
    pageNum: '1',
    pageSize: '10'
  };

  const result = await request({
    url: '/bssApi/common',
    method: 'post',
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      type: '1',
      api: 'com_sitech_crm_order_inter_IOrderQuerySvc_queryOrderLineExceptionList',
      params: body
    }
  });
  orderData.value = result.data?.ORDER_LINE_INFO_LIST;
}

async function handleOrder(orderLineId: string, type: number) {
  const body = {
    ORDER_LINE_ID: orderLineId,
    HANDLE_TYPE: type, // 1撤单，2重发
    LOGIN_NO: 'Z9MA41'
  };

  const result = await request({
    url: '/bssApi/common',
    method: 'post',
    data: {
      env: localStg.get('env'),
      platform: localStg.get('platform'),
      type: '1',
      api: 'com_sitech_crm_order_inter_IOrderLineInfoSvc_doOrderLineInfoException',
      params: body
    }
  });
  orderData.value = result.data.ORDER_LINE_INFO_LIST;
}

// 表单重置
async function resetForm() {
  orderForm.value.type = null;
  orderForm.value.time = null;
}
</script>

<template>
  <div class="min-h-500px flex-col-stretch gap-16px overflow-hidden lt-sm:overflow-auto">
    <NCard title="订单查询" size="small" :bordered="false" class="card-wrapper" hoverable>
      <!-- 查询条件 -->
      <NForm ref="formRef" label-placement="left" label-width="80" :model="orderForm" size="small">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 s:8 m:8" label="查询条件" path="type">
            <NInput
              v-model:value="orderForm.type"
              placeholder="订单号、子订单号、订单行号、服务号，默认查询当月订单..."
              round
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
              <NButton type="success" size="small" round @click="fetchInfo">
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
    </NCard>
    <!-- 数据表格 -->
    <NCard title="订单列表" :bordered="false" size="small" class="sm:flex-1-hidden card-wrapper" hoverable>
      <NDataTable
        :row-key="(row: any) => row.ORDER_ID"
        :columns="orderColumns"
        :data="orderData"
        size="small"
        class="sm:h-full"
      />
    </NCard>
  </div>
</template>
