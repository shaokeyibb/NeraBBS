import {createRouter, createWebHashHistory} from "vue-router";
import type {RouteRecordRaw} from "vue-router";
import type {PageDecl} from "../types/route.ts";

const buildRoutes = () => {
    const pages = import.meta.glob<PageDecl>("../pages/**/page.ts", {
        eager: true,
        import: "default",
    });

    const components = import.meta.glob("../pages/**/*.vue");

    const unresolvedRoutes = Object.keys(pages)
        .map((_path) => {
            const pageDecl = pages[_path];
            const path =
                _path.replace(/^\.\.\/pages/, "").replace(/\/page.ts$/, "") || "/";
            const component = components[_path.replace(/page.ts$/, "index.vue")];
            return {
                pageDecl,
                path,
                component,
            };
        })
        .filter((_route) => {
            if (_route.component === undefined) {
                console.warn(
                    "A page with page.js defined but no component found, skipped.",
                    _route,
                );
                return false;
            }
            return _route.component;
        });

    const resolveRoute: (
        route: (typeof unresolvedRoutes)[number],
    ) => RouteRecordRaw = (route) => {
        return {
            name:
                route.pageDecl.name ??
                (route.path.split("/").filter(Boolean).join("-") || "index"),
            path: route.path,
            component: route.component,
            meta: route.pageDecl.meta,
        };
    };

    const routes: RouteRecordRaw[] = [];

    const recursiveResolveRoute: (
        route?: (typeof unresolvedRoutes)[number],
    ) => void = (route) => {
        if (!route) {
            console.warn("Trying to resolve a route that does not exist, skipped.");
            return;
        }

        const resolvedRoute = resolveRoute(route);

        if (route.pageDecl.parent === resolvedRoute.name) {
            console.warn(
                "Parent route for",
                route,
                "is the same as the current route, skipped.",
            );
            unresolvedRoutes.splice(unresolvedRoutes.indexOf(route), 1);
            return;
        }

        // 如果没有 parent，直接加入已解析路由
        if (route.pageDecl.parent === undefined) {
            unresolvedRoutes.splice(unresolvedRoutes.indexOf(route), 1);
            routes.push(resolvedRoute);
            return;
        }

        // 如果有 parent，试图在已解析路由中找到 parent
        let resolvedParentRoute = routes.find(
            (r) => r.name === route.pageDecl.parent,
        );

        // 如果没有找到 parent，递归解析 parent
        if (resolvedParentRoute === undefined) {
            recursiveResolveRoute(
                unresolvedRoutes.find(
                    (r) => resolveRoute(r).name === route.pageDecl.parent,
                ),
            );
            resolvedParentRoute = routes.find(
                (r) => r.name === route.pageDecl.parent,
            );
        }

        // 如果 parent 依然没有找到，说明 parent 不存在
        if (resolvedParentRoute === undefined) {
            console.warn(
                "Parent route for",
                route,
                "is declared but not found, skipped.",
            );
            unresolvedRoutes.splice(unresolvedRoutes.indexOf(route), 1);
            return;
        }

        // 如果 parent 找到了，将当前路由加入 parent 的 children
        unresolvedRoutes.splice(unresolvedRoutes.indexOf(route), 1);
        resolvedParentRoute.children = resolvedParentRoute.children ?? [];
        resolvedParentRoute.children.push(resolvedRoute);
    };

    while (unresolvedRoutes.length > 0) {
        recursiveResolveRoute(unresolvedRoutes[0]);
    }

    return routes;
};

const router = createRouter({
    history: createWebHashHistory(),
    routes: buildRoutes(),
});

export default router;
