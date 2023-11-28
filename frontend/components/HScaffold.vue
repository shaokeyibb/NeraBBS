<script lang="ts" setup>
import {computed, useSlots, watchEffect} from "vue";
import {useMediaQuery} from "@vueuse/core";
import {HFloatingActionButton, HNavigationBar, HNavigationDrawer, HNavigationRail} from "halcyon-vue"

type SizeClass = 'compact' | 'medium' | 'expanded'
type Pane = 1 | 2

const props = withDefaults(defineProps<{
  initialSizeClass?: SizeClass | 'auto',
  initialPanes?: Pane | 'auto'
  floatingActionButtonLabel?: string,
  sizeClass: SizeClass,
  panes: Pane,
}>(), {
  initialSizeClass: 'auto',
  initialPanes: 'auto',
})

const emits = defineEmits<{
  'update:sizeClass': [value: SizeClass],
  'update:panes': [value: Pane],
}>()

defineSlots<{
  navigationBarContent(): any,
  navigationDrawerContent(): any,
  navigationRailContent(): any,
  defaultContent(): any,
  extraPaneContent(): any,
  floatingActionButtonIcon(): any,
}>()

const slots = useSlots()

const autoLayout = computed<SizeClass>(() => {
  if (props.initialSizeClass != "auto") {
    return props.initialSizeClass
  }

  if (useMediaQuery("(max-width: 600px)").value) {
    return 'compact'
  } else if (useMediaQuery("(max-width: 840px)").value) {
    return 'medium'
  } else {
    return 'expanded'
  }
})

const autoPanes = computed<Pane>(() => {
  if (props.initialPanes != "auto") {
    return props.initialPanes
  }

  return autoLayout.value === 'expanded' ? 2 : 1
})

watchEffect(() => {
  emits('update:sizeClass', autoLayout.value)
})

watchEffect(() => {
  emits('update:panes', autoPanes.value)
})

const mainPadding = computed(() => {
  let paddingTop = 0
  let paddingRight = 0
  let paddingBottom = 0
  let paddingLeft = 0

  if (props.sizeClass === 'compact') {
    paddingLeft += 16
    paddingRight += 16
  } else {
    paddingLeft += 24
    paddingRight += 24
  }

  if (props.sizeClass === 'compact' && 'navigationBarContent' in slots) {
    paddingBottom += 80
  }
  if (props.sizeClass !== 'compact' && 'navigationRailContent' in slots) {
    paddingLeft += 80
  }

  return `${paddingTop}px ${paddingRight}px ${paddingBottom}px ${paddingLeft}px`
})

</script>

<template>
  <template v-if="sizeClass === 'compact'">
    <h-navigation-bar v-if="'navigationBarContent' in $slots">
      <slot name="navigationBarContent"/>
    </h-navigation-bar>
    <!--  TODO: navigation drawer menu in Search component-->
    <h-navigation-drawer v-if="'navigationDrawerContent' in $slots">
      <slot name="navigationDrawerContent"/>
    </h-navigation-drawer>

    <h-floating-action-button v-if="'floatingActionButtonIcon' in $slots || floatingActionButtonLabel"
                              :label="floatingActionButtonLabel"
                              class="fab-btn"
                              kind="small">
      <slot name="floatingActionButtonIcon"/>
    </h-floating-action-button>
  </template>

  <template v-else-if="sizeClass === 'medium'">
    <h-navigation-rail v-if="'navigationRailContent' in $slots"
                       :fabLabel="floatingActionButtonLabel"
                       :hasDrawer="'navigationDrawerContent' in $slots"
                       :hasFab="'floatingActionButtonIcon' in $slots || floatingActionButtonLabel"
                       v-bind="$attrs">
      <template #content>
        <slot name="navigationRailContent"/>
      </template>
      <template v-if="'navigationDrawerContent' in $slots" #drawer>
        <slot name="navigationDrawerContent"/>
      </template>
      <template #fab-icon>
        <slot name="floatingActionButtonIcon"/>
      </template>
    </h-navigation-rail>
  </template>

  <template v-else-if="sizeClass === 'expanded'">
    <h-navigation-rail v-if="'navigationRailContent' in $slots"
                       :fabLabel="floatingActionButtonLabel"
                       :hasDrawer="'navigationDrawerContent' in $slots"
                       :hasFab="'floatingActionButtonIcon' in $slots || floatingActionButtonLabel"
                       v-bind="$attrs">
      <template #content>
        <slot name="navigationRailContent"/>
      </template>
      <template v-if="'navigationDrawerContent' in $slots" #drawer>
        <slot name="navigationDrawerContent"/>
      </template>
      <template #fab-icon>
        <slot name="floatingActionButtonIcon"/>
      </template>
    </h-navigation-rail>
  </template>

  <div v-if="panes === 2" class="main two-panes">
    <div class="pane default-pane overflow-y-auto">
      <slot name="defaultContent"/>
    </div>
    <div class="spacer"></div>
    <div class="pane extra-pane overflow-y-auto">
      <slot name="extraPaneContent"/>
    </div>
  </div>
  <div v-else class="main">
    <div class="pane default-pane">
      <slot name="defaultContent"/>
    </div>
  </div>
</template>

<style scoped>
.main {
  padding: v-bind(mainPadding);
}

.two-panes .default-pane {
  max-height: 100vh;
}

.two-panes .default-pane::-webkit-scrollbar {
  width: 3px;
  height: 3px;
}

.two-panes .default-pane::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 5px;
}

.main.two-panes {
  display: grid;
  grid-template-columns: 360px 24px 1fr;
}

.fab-btn {
  position: fixed;
  bottom: 80px;
  right: 0;
}

</style>