import type { PageDecl } from "../../types/route.ts";

export default {
  parent: "index",
  meta: {
    title: "page.trending.title",
    showInNavigationBar: {
      icon: "trending_up--outlined",
      activeIcon: "trending_up",
    },
  },
} satisfies PageDecl;
