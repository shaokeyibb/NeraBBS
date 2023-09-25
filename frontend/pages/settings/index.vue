<script lang="ts" setup>
import {NuxtLink} from "#components";
import {DefineComponent} from "vue";
import Profile from "~/components/settings/Profile.vue";
import Security from "~/components/settings/Security.vue";

const {t} = useI18n()
const router = useRouter()

const sizeClass = ref<'compact' | 'medium' | 'expanded'>('expanded')
const panes = ref<1 | 2>(2)

type Menu = {
  key: string,
  title: string
  subtitle: string
  component: DefineComponent<any, any, any, any, any>
}

const menus = shallowRef<Menu[]>([
  {
    key: "profile",
    title: t("settings.profile.title"),
    subtitle: t("settings.profile.subtitle"),
    component: Profile
  },
  {
    key: "security",
    title: t("settings.security.title"),
    subtitle: t("settings.security.subtitle"),
    component: Security
  }
])
const currentMenu = shallowRef<Menu | null>(null)

watch(currentMenu, (value) => {
  if (!value || panes.value != 1) return
  router.push(`/settings/${value.key}`)
})

watch(panes, (value) => {
  if (value !== 1 || !currentMenu.value) return
  router.push(`/settings/${currentMenu.value.key}`)
})
</script>

<template>
  <HScaffold v-model:panes="panes"
             v-model:sizeClass="sizeClass"
             initial-panes="auto"
             initialSizeClass="auto">
    <template #defaultContent>
      <HTopAppBar :title="$t('settings')" kind="small">
        <template #navigation-icon>
          <HIconButton :as="NuxtLink" :label="$t('home')" :noTooltip="true"
                       kind="standard"
                       to="/">
            <svg class="icon-[material-symbols--home]"/>
          </HIconButton>
        </template>
      </HTopAppBar>
      <div class="mx-5">
        <div class="flex flex-col">
          <div v-for="menu in menus" class="flex flex-col py-4" @click="currentMenu = menu">
            <h2>{{ menu.title }}</h2>
            <span class="text-sm mt-1">{{ menu.subtitle }}</span>
          </div>
        </div>
      </div>
    </template>
    <template #extraPaneContent>
      <component :is="currentMenu.component" v-if="currentMenu"/>
    </template>
  </HScaffold>
</template>