<script lang="ts" setup>
import type {Component} from 'vue'
import useLayout from "~/hooks/layout";
import {NuxtLink} from "#components";

const props = defineProps<{
  title: string,
  component: Component,
  backTo: string
}>()

const {getPanes} = useLayout()
const router = useRouter()

const panes = getPanes()

watch(panes, (value) => {
  if (value !== 2) return
  router.push(props.backTo)
})
</script>

<template>
  <HTopAppBar :title="title" kind="small">
    <template #navigation-icon>
      <HIconButton :as="NuxtLink" :label="$t('back')" :noTooltip="true"
                   :to="backTo"
                   kind="standard">
        <svg class="icon-[material-symbols--arrow-back]"/>
      </HIconButton>
    </template>
  </HTopAppBar>
  <component :is="component" v-bind="$attrs"/>
</template>