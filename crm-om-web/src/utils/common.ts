import { useClipboard } from '@vueuse/core';
import { $t } from '@/locales';

const { copy, isSupported } = useClipboard();

/**
 * Transform record to option
 *
 * @example
 *   ```ts
 *   const record = {
 *     key1: 'label1',
 *     key2: 'label2'
 *   };
 *   const options = transformRecordToOption(record);
 *   // [
 *   //   { value: 'key1', label: 'label1' },
 *   //   { value: 'key2', label: 'label2' }
 *   // ]
 *   ```;
 *
 * @param record
 */
export function transformRecordToOption<T extends Record<string, string>>(record: T) {
  return Object.entries(record).map(([value, label]) => ({
    value,
    label
  })) as CommonType.Option<keyof T>[];
}

/**
 * Translate options
 *
 * @param options
 */
export function translateOptions(options: CommonType.Option<string>[]) {
  return options.map(option => ({
    ...option,
    label: $t(option.label as App.I18n.I18nKey)
  }));
}

/**
 * Toggle html class
 *
 * @param className
 */
export function toggleHtmlClass(className: string) {
  function add() {
    document.documentElement.classList.add(className);
  }

  function remove() {
    document.documentElement.classList.remove(className);
  }

  return {
    add,
    remove
  };
}

/**
 * 报文复制
 *
 * @param copyContent {string} 需要复制的文本
 * @returns
 */
export function handleCopy(copyContent: string) {
  if (!isSupported) {
    window.$message?.error('您的浏览器不支持Clipboard API');
    return;
  }
  if (!copyContent) {
    window.$message?.error('复制的内容不能为空');
    return;
  }
  copy(copyContent);
  window.$message?.success(`复制成功`);
}

/**
 * 报文下载
 *
 * @param downloadContent 需要下载成文件的文本内容
 * @param fileName 文件名(需要自定义后缀)
 */
export function downloadFile(downloadContent: string, fileName: string) {
  const jsonStr = downloadContent;

  const url = window.URL || window.webkitURL || window;
  const blob = new Blob([jsonStr]);
  const saveLink: any = document.createElementNS('http://www.w3.org/1999/xhtml', 'a');

  saveLink.href = url.createObjectURL(blob);
  saveLink.download = fileName;
  saveLink.click();
}

/**
 * 检查当前运行环境是否为Mac OS。
 *
 * 这个函数通过检查navigator.userAgent字符串来判断当前运行环境。 如果userAgent字符串中包含"macintosh"或"mac os x"（不区分大小写），则认为当前环境是Mac OS。
 *
 * @returns {boolean} 如果当前环境是Mac OS，返回true，否则返回false。
 */
export function isMacOs(): boolean {
  const macRegex = /macintosh|mac os x/i;
  return macRegex.test(navigator.userAgent);
}

/**
 * 检查当前运行环境是否为Windows OS。
 *
 * 这个函数通过检查navigator.userAgent字符串来判断当前运行环境。 如果userAgent字符串中包含"windows"或"win32"（不区分大小写），则认为当前环境是Windows OS。
 *
 * @returns {boolean} 如果当前环境是Windows OS，返回true，否则返回false。
 */
export function isWindowsOs(): boolean {
  const windowsRegex = /windows|win32/i;
  return windowsRegex.test(navigator.userAgent);
}

/**
 * 判断是否是json字符串
 *
 * @param str 字符串
 */
export function isJSON(str: string) {
  try {
    const obj = JSON.parse(str);
    return Boolean(typeof obj === 'object' && obj);
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (e) {
    return false;
  }
}

/**
 * 将字符串转换为json字符串，如果转换失败，返回原字符串
 *
 * @param str 字符串
 */
export function str2JSON(str: string) {
  let result;
  try {
    const obj = JSON.parse(str);
    if (typeof obj === 'object' && obj) {
      result = JSON.stringify(obj, null, 2);
    }
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (e) {
    result = str;
  }
  return result;
}
