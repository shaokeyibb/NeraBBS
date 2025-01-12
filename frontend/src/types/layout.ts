import type {MaybeRef, MaybeRefOrGetter} from "@vueuse/core";
import type {ComputedRef} from "vue";

export type IconBtn = { icon: string; onClick: () => void };
export type IconBtnWithTooltip = IconBtn & { tooltip: MaybeRef<string> };

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
    fab?: IconBtn;
    topAppBar?: {
      leadingIconBtn?: IconBtn;
    };
    bottomAppBar?: {
      actions?: IconBtnWithTooltip[];
    };
  }) => void;
};
