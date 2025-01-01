import { createApp } from "vue";

import pinia from "./plugins/pinia.ts";
import i18n from "./plugins/vue-i18n.ts";
import router from "./plugins/vue-router.ts";
import head from "./plugins/unhead.ts";
import mdui from "./plugins/mdui.ts";
import masonryWall from "@yeger/vue-masonry-wall";

import "mdui/mdui.css";
import "mdui";

import "./reset.css";
import "./style.css";

import App from "./App.vue";

createApp(App)
  .use(pinia)
  .use(mdui)
  .use(head)
  .use(i18n)
  .use(router)
  .use(masonryWall)
  .mount("#app");
