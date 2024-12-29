import {allLocales} from "mdui/internal/localeCodes";
import type {LocaleCode} from "mdui";

export const messages = Object.fromEntries(
    Object.entries(
        import.meta.glob(["../assets/locales/*.json"], {
            eager: true,
            import: "default",
        }),
    )
        .map(([key, value]) => [
            key.match(/([a-z\-]+)\.json$/i)?.[1],
            value as Record<string, string>,
        ])
        .filter(([key]) => key !== undefined),
);

export const availableLocales = Object.keys(messages);

export const fallbackLocale = availableLocales[0];

const cultureLanguageMapping: Record<string, string> = {
    // 简体中文区
    zh: "zh-Hans",
    "zh-CN": "zh-Hans",
    "zh-CHS": "zh-Hans",
    "zh-SG": "zh-Hans",

    // 繁体中文区
    "zh-CHT": "zh-Hant",
    "zh-Hant-TW": "zh-Hant",
    "zh-TW": "zh-Hant",
    "zh-Hant-HK": "yue-Hant",
    "zh-Hant-MO": "yue-Hant",
    "zh-HK": "yue-Hant",
    "zh-MO": "yue-Hant",
    yue: "yue-Hant",

    // 日文区
    "ja-JP": "ja",

    // 俄文区
    "ru-RU": "ru",

    // 韩文区
    "ko-KR": "ko",

    // 越南语区
    "vi-VN": "vi",

    // 西班牙语区
    "es-ES": "es",
};

export const getLocalizedLocalName = (locale: string) => {
    return `${messages[locale].locale.name} (${messages[locale].locale.intl.name})`;
};

const matchLanguage = <
    Source extends string = string,
    Destination extends string = string,
>(
    source: Source,
    resultSet: Destination[],
    mapping: Record<Source, Destination>,
    fallback: Destination,
): Destination => {
    source.replace("_", "-");

    const sourceAsDestination = source as unknown as Destination;

    if (resultSet.includes(sourceAsDestination)) return sourceAsDestination;

    const exact = mapping[source];
    if (exact) return exact;

    const locale = new Intl.Locale(source);

    let candidate: {
        level: number;
        tag: string;
    } = {
        level: 0,
        tag: "",
    };
    for (const _key of resultSet) {
        const key = _key.replace("_", "-");
        const target = new Intl.Locale(key);

        if (target.language !== locale.language) continue;

        let level = 1;

        if (target.script && locale.script && target.script === locale.script)
            level++;
        if (target.region && locale.region && target.region === locale.region)
            level++;

        if (level > candidate.level) {
            candidate = {
                level,
                tag: _key,
            };
        }
    }

    return candidate.level < 1 ? fallback : (candidate.tag as Destination);
};

export const tryToMatchExistingLocale = (language: string) =>
    matchLanguage(
        language,
        availableLocales,
        cultureLanguageMapping,
        fallbackLocale,
    );

export const tryToConvertLocaleToMDUILocaleCode = (language: string) =>
    matchLanguage(
        language,
        allLocales as unknown as string[],
        {},
        "en-us",
    ) as LocaleCode;
