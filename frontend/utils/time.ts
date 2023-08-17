import {MaybeRefOrGetter, useTimeAgo, UseTimeAgoMessages} from "@vueuse/core";

const zhCNNumberMap = ['零', '一', '两', '三', '四', '五', '六', '七', '八', '九']

// Do not impl en-US caused it's default implementation in vue-use
const messages: Record<string, UseTimeAgoMessages<'second' | 'minute' | 'hour' | 'day' | 'week' | 'month' | 'year'>> = {
    "zh-CN": {
        justNow: '刚刚',
        past: n => n.match(/\d/) || [...n].some(it => it in zhCNNumberMap) ? `${n}前` : n,
        future: n => n.match(/\d/) || [...n].some(it => it in zhCNNumberMap) ? `${n}后` : n,
        month: (n) => `${zhCNNumberMap[n]}个月`,
        year: (n) => `${zhCNNumberMap[n]}年`,
        day: (n) => `${zhCNNumberMap[n]}天`,
        week: (n) => `${zhCNNumberMap[n]}周`,
        hour: n => `${n} 小时`,
        minute: n => `${n} 分钟`,
        second: n => `${n} 秒`,
        invalid: '',
    }
}

export function useTimeAgoLocalized(time: MaybeRefOrGetter<Date | number | string>) {

    const {locale} = useI18n()

    return useTimeAgo(time, {
        max: "week",
        fullDateFormatter: (date) => date.toLocaleTimeString(locale.value),
        messages: messages[locale.value]
    })
}