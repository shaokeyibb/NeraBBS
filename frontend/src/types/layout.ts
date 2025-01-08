import type { MaybeRefOrGetter } from "@vueuse/core";
import type { ComputedRef } from "vue";

export type Fab = { icon: string; onClick: () => void };

export type Layout = {
  isLargeScreen: ComputedRef<boolean>;
  navigationRail: MaybeRefOrGetter<
    | {
        width: number;
      }
    | undefined
  >;
  navigationBar: MaybeRefOrGetter<
    | {
        height: number;
      }
    | undefined
  >;
  topAppBar: MaybeRefOrGetter<
    | {
        height: number;
      }
    | undefined
  >;
  updateLayout: (data: {
    fab?: {
      icon: string;
      onClick: () => void;
    };
  }) => void;
};
