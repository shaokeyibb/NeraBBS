<script setup lang="ts">
import type { Component } from "vue";
import { inject, ref, shallowRef, useTemplateRef } from "vue";
import type { Layout } from "../../types/layout.ts";
import { layout } from "../../utils/symbol.ts";
import MasonryWall from "@yeger/vue-masonry-wall";
import SettingsProfile from "./SettingsProfile.vue";
import NCard from "../../components/NCard.vue";
import NCardHeader from "../../components/NCardHeader.vue";
import { useI18n } from "vue-i18n";
import SettingsSecurity from "./SettingsSecurity.vue";
import NCardMain from "../../components/NCardMain.vue";

const { t } = useI18n();

const wallContainer = useTemplateRef<HTMLDivElement>("wall-container");

const items = shallowRef<
  {
    key: string;
    title: string;
    subtitle?: string;
    component: Component;
  }[]
>([
  {
    key: "profile",
    title: "page.settings.profile.title",
    subtitle: "page.settings.profile.subtitle",
    component: SettingsProfile,
  },
  {
    key: "security",
    title: "page.settings.security.title",
    subtitle: "page.settings.security.subtitle",
    component: SettingsSecurity,
  },
]);

const currentTab = ref<string>(items.value[0].key);

const l = inject(layout) as Layout;
</script>

<template>
  <div ref="wall-container" class="wall-container" v-if="l.isLargeScreen.value">
    <MasonryWall
      :columnWidth="564"
      :gap="8"
      :items="items"
      :scroll-container="wallContainer"
    >
      <template #default="{ item }">
        <NCard>
          <NCardHeader
            :header="t(item.title)"
            :subhead="item.subtitle == undefined ? undefined : t(item.subtitle)"
          />
          <NCardMain>
            <component :is="item.component" inside-card />
          </NCardMain>
        </NCard>
      </template>
    </MasonryWall>
  </div>
  <div v-else>
    <mdui-tabs :value="currentTab" @change="currentTab = $event.target.value">
      <mdui-tab v-for="item in items" :key="item.key" :value="item.key">
        {{ t(item.title) }}
      </mdui-tab>

      <mdui-tab-panel
        slot="panel"
        v-for="item in items"
        :key="item.key"
        :value="item.key"
        class="tab-panel"
      >
        <component :is="item.component" />
      </mdui-tab-panel>
    </mdui-tabs>
  </div>
</template>

<style scoped>
.wall-container {
  display: flex;
  flex-direction: column;

  box-sizing: border-box;
  overflow-y: auto;
  padding: 16px 24px;

  user-select: none;
}

.tab-panel {
  margin: 16px 8px;
}
</style>
