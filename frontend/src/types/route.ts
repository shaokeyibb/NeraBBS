import type {RouteMeta, RouteRecordNameGeneric} from "vue-router";

export interface PageDecl {
    name?: RouteRecordNameGeneric;
    parent?: NonNullable<RouteRecordNameGeneric>;
    meta: RouteMeta;
}

type ShowInNavigationBar = {
    icon: string;
    activeIcon: string;
};

interface RouteMetaBase {
    title?: string;
}

interface RouteMetaWithNav extends RouteMetaBase {
    showInNavigationBar: ShowInNavigationBar;
    title: string;
}

interface RouteMetaWithoutNav extends RouteMetaBase {
    showInNavigationBar?: undefined;
}

declare module "vue-router" {
    interface RouteMeta extends RouteMetaWithNav, RouteMetaWithoutNav {
    }
}
