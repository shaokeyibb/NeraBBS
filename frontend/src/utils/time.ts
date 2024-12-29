import type {MaybeRefOrGetter, UseTimeAgoMessages} from "@vueuse/core";
import {useTimeAgo} from "@vueuse/core";
import useLocale from "../hooks/locale.ts";

const zhCNNumberMap = [
    "零",
    "一",
    "两",
    "三",
    "四",
    "五",
    "六",
    "七",
    "八",
    "九",
];

// Do not impl en-US caused it's default implementation in vue-use
const messages: Record<
    string,
    UseTimeAgoMessages<
        "second" | "minute" | "hour" | "day" | "week" | "month" | "year"
    >
> = {
    "zh-Hans": {
        justNow: "刚刚",
        past: (n) =>
            n.match(/\d/) || [...n].some((it) => zhCNNumberMap.indexOf(it) != -1)
                ? `${n}前`
                : n,
        future: (n) =>
            n.match(/\d/) || [...n].some((it) => zhCNNumberMap.indexOf(it) != -1)
                ? `${n}后`
                : n,
        month: (n) => `${zhCNNumberMap[n]}个月`,
        year: (n) => `${zhCNNumberMap[n]}年`,
        day: (n) => `${zhCNNumberMap[n]}天`,
        week: (n) => `${zhCNNumberMap[n]}周`,
        hour: (n) => `${n} 小时`,
        minute: (n) => `${n} 分钟`,
        second: (n) => `${n} 秒`,
        invalid: "",
    },
};

export function useTimeAgoLocalized(
    time: MaybeRefOrGetter<Date | number | string>,
) {
    const {locale} = useLocale();

    return useTimeAgo(time, {
        max: "week",
        fullDateFormatter: (date) => date.toLocaleString(locale.value),
        messages: messages[locale.value],
    });
}
