import type { PageDecl } from "../../types/route.ts";

export default {
  parent: "index",
  meta: {
    title: "page.create.title",
    requireUserSession: "MUST_SIGNED_IN",
  },
} satisfies PageDecl;
