import type { PageDecl } from "../../types/route.ts";

export default {
  parent: "index",
  meta: {
    title: "page.sign_in.title",
    requireUserSession: "MUST_NOT_SIGNED_IN",
  },
} satisfies PageDecl;
