/* eslint-disable */
/* prettier-ignore */
// Generated by elegant-router
// Read more: https://github.com/soybeanjs/elegant-router

import type { GeneratedRoute } from '@elegant-router/types';

export const generatedRoutes: GeneratedRoute[] = [
  {
    name: '403',
    path: '/403',
    component: 'layout.blank$view.403',
    meta: {
      title: '403',
      i18nKey: 'route.403',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: '404',
    path: '/404',
    component: 'layout.blank$view.404',
    meta: {
      title: '404',
      i18nKey: 'route.404',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: '500',
    path: '/500',
    component: 'layout.blank$view.500',
    meta: {
      title: '500',
      i18nKey: 'route.500',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: 'about',
    path: '/about',
    component: 'layout.base$view.about',
    meta: {
      title: 'about',
      i18nKey: 'route.about'
    }
  },
  {
    name: 'home',
    path: '/home',
    component: 'layout.base$view.home',
    meta: {
      title: 'home',
      i18nKey: 'route.home',
      icon: 'mdi:monitor-dashboard',
      order: 1
    }
  },
  {
    name: 'iframe-page',
    path: '/iframe-page/:url',
    component: 'layout.base$view.iframe-page',
    props: true,
    meta: {
      title: 'iframe-page',
      i18nKey: 'route.iframe-page',
      constant: true,
      hideInMenu: true,
      keepAlive: true
    }
  },
  {
    name: 'login',
    path: '/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?',
    component: 'layout.blank$view.login',
    props: true,
    meta: {
      title: 'login',
      i18nKey: 'route.login',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: 'manage',
    path: '/manage',
    component: 'layout.base',
    meta: {
      title: 'manage',
      i18nKey: 'route.manage'
    },
    children: [
      {
        name: 'manage_menu',
        path: '/manage/menu',
        component: 'view.manage_menu',
        meta: {
          title: 'manage_menu',
          i18nKey: 'route.manage_menu'
        }
      },
      {
        name: 'manage_role',
        path: '/manage/role',
        component: 'view.manage_role',
        meta: {
          title: 'manage_role',
          i18nKey: 'route.manage_role'
        }
      },
      {
        name: 'manage_user',
        path: '/manage/user',
        component: 'view.manage_user',
        meta: {
          title: 'manage_user',
          i18nKey: 'route.manage_user'
        }
      },
      {
        name: 'manage_user-detail',
        path: '/manage/user-detail/:id',
        component: 'view.manage_user-detail',
        meta: {
          title: 'manage_user-detail',
          i18nKey: 'route.manage_user-detail'
        }
      }
    ]
  },
  {
    name: 'operation',
    path: '/operation',
    component: 'layout.base',
    meta: {
      title: 'operation',
      i18nKey: 'route.operation'
    },
    children: [
      {
        name: 'operation_abnormal-order',
        path: '/operation/abnormal-order',
        component: 'view.operation_abnormal-order',
        meta: {
          title: 'operation_abnormal-order',
          i18nKey: 'route.operation_abnormal-order'
        }
      },
      {
        name: 'operation_noc-info',
        path: '/operation/noc-info',
        component: 'view.operation_noc-info',
        meta: {
          title: 'operation_noc-info',
          i18nKey: 'route.operation_noc-info'
        }
      },
      {
        name: 'operation_order-info',
        path: '/operation/order-info',
        component: 'view.operation_order-info',
        meta: {
          title: 'operation_order-info',
          i18nKey: 'route.operation_order-info'
        }
      }
    ]
  },
  {
    name: 'other',
    path: '/other',
    component: 'layout.base',
    meta: {
      title: 'other',
      i18nKey: 'route.other'
    },
    children: [
      {
        name: 'other_data',
        path: '/other/data',
        component: 'view.other_data',
        meta: {
          title: 'other_data',
          i18nKey: 'route.other_data'
        }
      },
      {
        name: 'other_string',
        path: '/other/string',
        component: 'view.other_string',
        meta: {
          title: 'other_string',
          i18nKey: 'route.other_string'
        }
      }
    ]
  },
  {
    name: 'prod-config',
    path: '/prod-config',
    component: 'layout.base',
    meta: {
      title: 'prod-config',
      i18nKey: 'route.prod-config'
    },
    children: [
      {
        name: 'prod-config_config-script',
        path: '/prod-config/config-script',
        component: 'view.prod-config_config-script',
        meta: {
          title: 'prod-config_config-script',
          i18nKey: 'route.prod-config_config-script'
        }
      }
    ]
  },
  {
    name: 'user-center',
    path: '/user-center',
    component: 'layout.base$view.user-center',
    meta: {
      title: 'user-center',
      i18nKey: 'route.user-center'
    }
  }
];
