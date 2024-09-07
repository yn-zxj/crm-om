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
