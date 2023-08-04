/** @type {import('tailwindcss').Config} */
import colors from "tailwindcss/colors";

module.exports = {
    content: [
        "./components/**/*.{js,vue,ts}",
        "./layouts/**/*.vue",
        "./pages/**/*.vue",
        "./plugins/**/*.{js,ts}",
        "./nuxt.config.{js,ts}",
        "./app.vue",
    ],
    theme: {
        extend: {
            colors: {
                primary: '#FF5252',
                secondary: '#CDDC39',
                tertiary: '#6750A4',
                neutral: colors.neutral
            }
        },
    },
    plugins: [
        require('@iconify/tailwind').addDynamicIconSelectors()
    ],
    darkMode: 'class',
}