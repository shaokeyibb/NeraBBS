/** @type {import('tailwindcss').Config} */
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
                'halcyon': {
                    'primary': 'var(--halcyon-primary)',
                    'on-primary': 'var(--halcyon-on-primary)',
                    'primary-hover': 'var(--halcyon-primary-hover)',

                    'secondary': 'var(--halcyon-secondary)',
                    'on-secondary': 'var(--halcyon-on-secondary)',
                    'secondary-hover': 'var(--halcyon-secondary-hover)',

                    'tertiary': 'var(--halcyon-tertiary)',
                    'on-tertiary': 'var(--halcyon-on-tertiary)',
                    'tertiary-hover': 'var(--halcyon-tertiary-hover)',

                    'background': 'var(--halcyon-background)',
                    'on-background': 'var(--halcyon-on-background)',

                    'surface': 'var(--halcyon-surface)',
                    'on-surface': 'var(--halcyon-on-surface)',

                    'error': 'var(--halcyon-error)',
                    'on-error': 'var(--halcyon-on-error)',

                    'surface-variant': 'var(--halcyon-surface-variant)',
                    'on-surface-variant': 'var(--halcyon-on-surface-variant)',

                    'inverse-surface': 'var(--halcyon-inverse-surface)',
                    'inverse-on-surface': 'var(--halcyon-inverse-on-surface)',

                    'inverse-primary': 'var(--halcyon-inverse-primary)',

                    'shadow': 'var(--halcyon-shadow)',

                    'surface-tint': 'var(--halcyon-surface-tint)',

                    'outline': 'var(--halcyon-outline)',
                    'outline-variant': 'var(--halcyon-outline-variant)',
                },
            },
        },
    },
    plugins: [
        require('@iconify/tailwind').addDynamicIconSelectors(),
    ],
}