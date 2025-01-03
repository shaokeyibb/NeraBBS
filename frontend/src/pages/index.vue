<script lang="ts" setup>
import {NavigationDrawer} from "mdui";
import NText from "../components/NText.vue";
import {useRoute, useRouter} from "vue-router";
import {computed, provide, useTemplateRef, watchEffect,} from "vue";
import {useI18n} from "vue-i18n";
import {useMediaQuery} from "@vueuse/core";
import useTheme from "../hooks/theme.ts";
import useLocale from "../hooks/locale.ts";
import {availableLocales, getLocalizedLocalName} from "../utils/locale.ts";
import {layout} from "../utils/symbol.ts";
import useAuth from "../hooks/auth.ts";
import {useSessionStore} from "../stores/session.ts";
import {storeToRefs} from "pinia";

const { t } = useI18n();
const router = useRouter();
const route = useRoute();

const { theme, toggleTheme } = useTheme();
const { locale, setLocale } = useLocale();
const { signOut, isLoggedIn } = useAuth();
const { userProfile } = storeToRefs(useSessionStore());

const isLargeScreen = useMediaQuery("(min-width: 1024px)");

const navigationItems = computed(() =>
  route.matched[0].children
    .filter(
      (child) =>
        child.meta !== undefined &&
        child.meta.showInNavigationBar !== undefined,
    )
    .map((child) => ({
      name: child.name,
      title: child.meta!.title,
      icon: child.meta!.showInNavigationBar.icon,
      activeIcon: child.meta!.showInNavigationBar.activeIcon,
    })),
);

const navigationDrawer = useTemplateRef<NavigationDrawer>("navigationDrawer");

watchEffect(() => {
  if (route.matched.length === 1 && route.matched[0].name === "index") {
    router.replace({ name: "explore" });
  }
});

const l = {
  navigationRail: isLargeScreen.value ? { width: 80 } : undefined,
  navigationBar: !isLargeScreen.value ? { height: 80 } : undefined,
  topAppBar: { height: 64 },
};

provide(layout, l);

const navigationBarHeight = (l.navigationBar?.height ?? 0) + "px";
const topAppBarHeight = l.topAppBar.height + "px";
</script>

<template>
  <mdui-layout>
    <mdui-navigation-rail
      v-if="isLargeScreen"
      :value="route.name"
      alignment="center"
      @change="router.replace({ name: $event.target.value })"
    >
      <mdui-fab slot="top" icon="add--outlined"></mdui-fab>
      <mdui-navigation-rail-item
        v-for="item in navigationItems"
        :key="item.name"
        :active-icon="item.activeIcon"
        :icon="item.icon"
        :value="item.name"
        >{{ t(item.title) }}
      </mdui-navigation-rail-item>
      <mdui-dropdown slot="bottom">
        <mdui-button-icon slot="trigger" icon="language" />
        <mdui-menu>
          <mdui-menu-item disabled
            >{{ t("locale.switcher.tooltip") }}
          </mdui-menu-item>
          <mdui-menu-item
            v-for="_locale in availableLocales"
            :key="_locale"
            @click="setLocale(_locale)"
          >
            {{ locale === _locale ? "✔" : "" }}
            {{ getLocalizedLocalName(_locale) }}
          </mdui-menu-item>
        </mdui-menu>
      </mdui-dropdown>
      <mdui-tooltip
        v-show="theme === 'auto'"
        slot="bottom"
        :content="t('theme.switcher.tooltip', { theme: t('theme.auto') })"
      >
        <mdui-button-icon icon="mode_standby" @click="toggleTheme()" />
      </mdui-tooltip>
      <mdui-tooltip
        v-show="theme === 'light'"
        slot="bottom"
        :content="t('theme.switcher.tooltip', { theme: t('theme.light') })"
      >
        <mdui-button-icon icon="light_mode" @click="toggleTheme()" />
      </mdui-tooltip>
      <mdui-tooltip
        v-show="theme === 'dark'"
        slot="bottom"
        :content="t('theme.switcher.tooltip', { theme: t('theme.dark') })"
      >
        <mdui-button-icon icon="dark_mode" @click="toggleTheme()" />
      </mdui-tooltip>
    </mdui-navigation-rail>
    <template v-else>
      <mdui-navigation-bar
        :value="route.name"
        @change="router.replace({ name: $event.target.value })"
      >
        <mdui-navigation-bar-item
          v-for="item in navigationItems"
          :key="item.name"
          :active-icon="item.activeIcon"
          :icon="item.icon"
          :value="item.name"
          >{{ t(item.title) }}
        </mdui-navigation-bar-item>
      </mdui-navigation-bar>
    </template>
    <mdui-top-app-bar class="top-app-bar">
      <mdui-icon name="alternate_email"></mdui-icon>
      <mdui-top-app-bar-title>{{ t("site.title") }}</mdui-top-app-bar-title>
      <mdui-text-field
        :label="t('search.label')"
        class="search-bar"
        clearable
        variant="outlined"
      >
        <mdui-button-icon slot="end-icon" icon="search" />
      </mdui-text-field>
      <template v-if="isLargeScreen">
        <mdui-dropdown v-if="isLoggedIn">
          <mdui-avatar :src="userProfile?.avatarPath" slot="trigger" />
          <mdui-menu>
            <mdui-menu-item @click="router.push('settings')">
              <n-text>{{ t("settings") }}</n-text>
            </mdui-menu-item>
            <mdui-menu-item @click="signOut()">
              <n-text>{{ t("sign_out") }}</n-text>
            </mdui-menu-item>
          </mdui-menu>
        </mdui-dropdown>
        <mdui-tooltip v-else :content="t('sign_in')">
          <router-link :to="{ name: 'signin' }">
            <mdui-button-icon>
              <mdui-icon name="login" />
            </mdui-button-icon>
          </router-link>
        </mdui-tooltip>
      </template>
      <mdui-button-icon
        v-else
        icon="menu"
        @click="navigationDrawer!.open = true"
      />
    </mdui-top-app-bar>
    <mdui-navigation-drawer
      ref="navigationDrawer"
      close-on-esc
      close-on-overlay-click
      modal
      placement="right"
    >
      <mdui-list>
        <mdui-collapse>
          <mdui-collapse-item>
            <mdui-list-item slot="header" icon="language"
              >{{ t("locale.switcher.tooltip") }}
            </mdui-list-item>
            <div style="margin-left: 2.5rem">
              <mdui-list-item
                v-for="_locale in availableLocales"
                :key="_locale"
                @click="setLocale(_locale)"
              >
                {{ locale === _locale ? "✔" : "" }}
                {{ getLocalizedLocalName(_locale) }}
              </mdui-list-item>
            </div>
          </mdui-collapse-item>
        </mdui-collapse>
        <mdui-list-item
          :icon="
            { auto: 'mode_standby', light: 'light_mode', dark: 'dark_mode' }[
              theme
            ]
          "
          @click="toggleTheme()"
          >{{ t("theme.switcher.tooltip", { theme: t(`theme.${theme}`) }) }}
        </mdui-list-item>
      </mdui-list>
      <mdui-divider />
      <mdui-list>
        <mdui-collapse>
          <mdui-collapse-item>
            <template v-if="isLoggedIn">
              <mdui-list-item icon="login" slot="header">
                {{ userProfile?.username }}
                <mdui-avatar slot="icon" :src="userProfile?.avatarPath" />
              </mdui-list-item>
              <div style="margin-left: 2.5rem">
                <mdui-list-item @click="router.push('settings')">
                  <n-text>{{ t("settings") }}</n-text>
                </mdui-list-item>
                <mdui-list-item @click="signOut()">
                  <n-text>{{ t("sign_out") }}</n-text>
                </mdui-list-item>
              </div>
            </template>
            <mdui-list-item
              icon="login"
              slot="header"
              v-else
              @click="router.push('signin')"
              >{{ t("sign_in") }}
            </mdui-list-item>
          </mdui-collapse-item>
        </mdui-collapse>
      </mdui-list>
    </mdui-navigation-drawer>
    <mdui-layout-main>
      <RouterView />
      <mdui-fab v-if="!isLargeScreen" class="fab" icon="add" />
    </mdui-layout-main>
  </mdui-layout>
</template>

<style scoped>
mdui-layout-main {
  height: 100vh;
  overflow: hidden;
}

.fab {
  position: fixed;
  right: 16px;
  bottom: calc(16px + v-bind(navigationBarHeight));
}

.top-app-bar {
  align-items: center;
}

.search-bar {
  width: calc(50vw - v-bind(navigationBarHeight) / 2);
  height: calc(
    v-bind(topAppBarHeight) - 24px
  ); /* 64px is the height of the top app bar, 24px is the y-axis padding */
  --mdui-typescale-body-large-line-height: 0.75rem;
  --mdui-typescale-body-large-size: 0.75rem;
}
</style>
