import type {InjectionKey} from "vue";

export const isInNCard = Symbol() as InjectionKey<boolean>;

export const layout = Symbol() as InjectionKey<{
    navigationRail?: {
        width: number;
    };
    navigationBar?: {
        height: number;
    };
    topAppBar?: {
        height: number;
    };
}>;
