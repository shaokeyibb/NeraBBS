import type { PageDecl } from "../../types/route.ts";

export default {
  parent: "index",
  meta: {
    title: "page.settings.title",
    requireUserSession: "MUST_SIGNED_IN",
  },
} satisfies PageDecl;
