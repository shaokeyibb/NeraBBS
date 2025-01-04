import type {InjectionKey} from "vue";
import type {Layout} from "../types/layout.ts";

export const isInNCard = Symbol() as InjectionKey<boolean>;

export const layout = Symbol() as InjectionKey<Layout>;
