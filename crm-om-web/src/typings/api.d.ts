/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  namespace Common {
    /** common params of paginating */
    interface PaginatingCommonParams {
      /** current page number */
      current: number;
      /** page size */
      size: number;
      /** total count */
      total: number;
    }

    /** common params of paginating query list data */
    interface PaginatingQueryRecord<T = any> extends PaginatingCommonParams {
      records: T[];
    }

    /** common search params of table */
    type CommonSearchParams = Pick<Common.PaginatingCommonParams, 'current' | 'size'>;

    /**
     * enable status
     *
     * - "0": disabled
     * - "1": enabled
     */
    type EnableStatus = '0' | '1';

    /** common record */
    type CommonRecord<T = any> = {
      /** record id */
      id: number;
      /** record creator */
      createBy: string;
      /** record create time */
      createTime: string;
      /** record updater */
      updateBy: string;
      /** record update time */
      updateTime: string;
      /** record status */
      status: EnableStatus | null;
    } & T;
  }

  /**
   * namespace Auth
   *
   * backend api module: "auth"
   */
  namespace Auth {
    interface LoginToken {
      token: string;
      refreshToken: string;
    }

    interface UserInfo {
      userId: string;
      userName: string;
      roles: string[];
      buttons: string[];
    }
  }

  /**
   * namespace Route
   *
   * backend api module: "route"
   */
  namespace Route {
    type ElegantConstRoute = import('@elegant-router/types').ElegantConstRoute;

    interface MenuRoute extends ElegantConstRoute {
      id: string;
    }

    interface UserRoute {
      routes: MenuRoute[];
      home: import('@elegant-router/types').LastLevelRouteKey;
    }
  }

  // 下面新增
  /**
   * namespace SystemManage
   *
   * backend api module: "systemManage"
   */
  namespace SystemManage {
    type CommonSearchParams = Pick<Common.PaginatingCommonParams, 'current' | 'size'>;

    /** role */
    type Role = Common.CommonRecord<{
      /** role name */
      roleName: string;
      /** role code */
      roleCode: string;
      /** role description */
      roleDesc: string;
    }>;

    /** role search params */
    type RoleSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.Role, 'roleName' | 'roleCode' | 'status'> & CommonSearchParams
    >;

    /** role list */
    type RoleList = Common.PaginatingQueryRecord<Role>;

    /** all role */
    type AllRole = Pick<Role, 'id' | 'roleName' | 'roleCode'>;

    /**
     * user gender
     *
     * - "1": "male"
     * - "2": "female"
     */
    type UserGender = '1' | '2';

    /** user */
    type User = Common.CommonRecord<{
      /** user name */
      userName: string;
      /** user gender */
      userGender: UserGender | null;
      /** user nick name */
      nickName: string;
      /** user phone */
      userPhone: string;
      /** user email */
      userEmail: string;
      /** user role code collection */
      userRoles: string[];
    }>;

    /** user search params */
    type UserSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.User, 'userName' | 'userGender' | 'nickName' | 'userPhone' | 'userEmail' | 'status'> &
        CommonSearchParams
    >;

    /** user list */
    type UserList = Common.PaginatingQueryRecord<User>;

    /**
     * menu type
     *
     * - "0": directory
     * - "1": menu
     */
    type MenuType = '0' | '1';

    type MenuButton = {
      /**
       * button code
       *
       * it can be used to control the button permission
       */
      code: string;
      /** button description */
      desc: string;
    };

    /**
     * icon type
     *
     * - "1": iconify icon
     * - "2": local icon
     */
    type IconType = '1' | '2';

    type MenuPropsOfRoute = Pick<
      import('vue-router').RouteMeta,
      | 'i18nKey'
      | 'keepAlive'
      | 'constant'
      | 'order'
      | 'href'
      | 'hideInMenu'
      | 'activeMenu'
      | 'multiTab'
      | 'fixedIndexInTab'
      | 'query'
    >;

    type Menu = Common.CommonRecord<{
      /** parent menu id */
      parentId: number;
      /** menu type */
      menuType: MenuType;
      /** menu name */
      menuName: string;
      /** route name */
      routeName: string;
      /** route path */
      routePath: string;
      /** component */
      component?: string;
      /** iconify icon name or local icon name */
      icon: string;
      /** icon type */
      iconType: IconType;
      /** buttons */
      buttons?: MenuButton[] | null;
      /** children menu */
      children?: Menu[] | null;
    }> &
      MenuPropsOfRoute;

    /** menu list */
    type MenuList = Common.PaginatingQueryRecord<Menu>;

    type MenuTree = {
      id: number;
      label: string;
      pId: number;
      children?: MenuTree[];
    };

    /** log */
    type Log = Common.CommonRecord<{
      opId: string;
      title: string;
      opUrl: string;
      method: string;
      costTime: string;
      opTime: string;
      businessType: string;
      requestMethod: string;
      opType: string;
      opParam: string;
      opResult: string;
    }>;

    /** log search params */
    type LogSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.Log, 'businessType' | 'requestMethod' | 'opType' | 'status'> & CommonSearchParams
    >;

    /** log list */
    type LogList = Common.PaginatingQueryRecord<Log>;

    /** log */
    type Login = Common.CommonRecord<{
      infoId: string;
      userId: string;
      ip: string;
      msg: string;
      accessTime: string;
    }>;

    /** login search params */
    type LoginSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.Login, 'infoId' | 'userId' | 'ip' | 'msg' | 'accessTime' | 'status'> & CommonSearchParams
    >;

    /** login list */
    type LoginList = Common.PaginatingQueryRecord<Login>;

    /** config */
    type Config = Common.CommonRecord<{
      configId: string;
      configName: string;
      configKey: string;
      configValue: string;
      remark: string;
      configType: string;
    }>;

    /** config search params */
    type ConfigSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.Config, 'configName' | 'configKey' | 'configType' | 'status'> & CommonSearchParams
    >;

    /** config list */
    type ConfigList = Common.PaginatingQueryRecord<Config>;

    /** dict type */
    type DictType = Common.CommonRecord<{
      /* 主键 */
      dictId: string;
      /* 字典名称 */
      dictName: string;
      /* 字典类型 */
      dictType: string;
      /* 备注 */
      remark: string;
    }>;

    /** dict type search params */
    type DictTypeSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.DictType, 'dictName' | 'dictType' | 'status'> & CommonSearchParams
    >;

    /** dict type list */
    type DictTypeList = Common.PaginatingQueryRecord<DictType>;

    /** dict data */
    type DictData = Common.CommonRecord<{
      /* 主键 */
      dictCode: string;
      /* 字典类型 */
      dictType: string;
      /* 字典排序 */
      dictSort: string;
      /* 字典标签 */
      dictLabel: string;
      /* 字典键值 */
      dictValue: string;
      /* 状态 */
      status: string;
      /* 备注 */
      remark: string;
    }>;

    /** dict data search params */
    type DictDataSearchParams = CommonType.RecordNullable<
      Pick<Api.SystemManage.DictData, 'dictCode' | 'dictType'> & CommonSearchParams
    >;

    /** dict data list */
    type DictDataList = Common.PaginatingQueryRecord<DictData>;
  }
}
