import type { PageDecl } from "../../types/route.ts";

export default {
  parent: "index",
  meta: {
    title: "page.explore.title",
    showInNavigationBar: {
      icon: "home--outlined",
      activeIcon: "home",
    },
  },
} satisfies PageDecl;
