import { defineConfig, UserConfig } from "vite";
import vue from "@vitejs/plugin-vue";

import autoprefixer from "autoprefixer";

// https://vite.dev/config/
export default defineConfig(({ command }) => {
  let base: UserConfig = {
    plugins: [
      vue({
        template: {
          compilerOptions: {
            isCustomElement: (tag) => tag.startsWith("mdui-"),
          },
        },
      }),
    ],
    css: {
      postcss: {
        plugins: [autoprefixer],
      },
    },
    build: {
      rollupOptions: {
        output: {
          manualChunks: {
            "vueuse-core": ["@vueuse/core"],
            mdui: ["mdui"],
          },
        },
      },
    },
  };

  if (command === "serve") {
    base = {
      ...base,
      server: {
        proxy: {
          "/api": {
            target: "http://localhost:8080/api",
            changeOrigin: true,
            rewrite: (path) => path.replace(/^\/api/, ""),
          },
        },
      },
    };
  }

  return base;
});
