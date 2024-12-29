import globals from "globals";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";
import eslintConfigPrettier from "eslint-config-prettier";

/** @type {import("eslint").Linter.Config[]} */
export default [
    {files: ["**/*.{js,mjs,cjs,ts,vue}"]},
    {languageOptions: {globals: globals.browser}},
    ...tseslint.configs.recommended,
    ...pluginVue.configs["flat/essential"],
    {
        files: ["**/*.vue"],
        languageOptions: {parserOptions: {parser: tseslint.parser}},
    },
    eslintConfigPrettier,
    {
        rules: {
            "vue/multi-word-component-names": ["error", {ignores: ["index"]}],
            "vue/no-deprecated-slot-attribute": "off",
        },
    },
    {
        ignores: ["dist/**/*", "build/**/*"],
    },
];
