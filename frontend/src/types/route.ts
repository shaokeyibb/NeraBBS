import type { RouteMeta, RouteRecordNameGeneric } from "vue-router";

export interface PageDecl {
  name?: RouteRecordNameGeneric;
  parent?: NonNullable<RouteRecordNameGeneric>;
  meta?: RouteMeta;
}

type ShowInNavigationBar = {
  icon: string;
  activeIcon: string;
};

declare module "vue-router" {
  interface RouteMeta {
    title?: string;
    showInNavigationBar?: ShowInNavigationBar;
    requireUserSession?: "MUST_SIGNED_IN" | "MUST_NOT_SIGNED_IN";
  }
}
