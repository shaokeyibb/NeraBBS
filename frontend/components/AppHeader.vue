<script lang="ts" setup>
import useLocale from "~/hooks/locale";
import useDarkMode from "~/hooks/dark_mode";
import {storeToRefs} from "pinia";
import {useUserInfoStore} from "~/stores/user_info";
import {useDarkModeStore} from "~/stores/dark_mode";
import useUser from "~/hooks/user";

const {$pinia} = useNuxtApp()

const {availableLocales} = useI18n()

const {getLocalizedLocalName, setLocalePermanently} = useLocale()

const {mode} = storeToRefs(useDarkModeStore())
const {toggleDarkMode} = useDarkMode($pinia)

const {userInfo} = storeToRefs(useUserInfoStore())
const {signout} = useUser($pinia)

const isMenuDrawerOpen = ref(false)
const isMobileLanguageSelectorDrawerOpen = ref(false)
const isMobileUserDrawerOpen = ref(false)
</script>

<template>
  <div class="container mx-auto p-4 hidden lg:flex flex-row justify-between items-center bg-white dark:bg-black">
    <div>
      <NuxtLink class="dark:text-white" to="/">NeraBBS</NuxtLink>
    </div>
    <div>
      <button class="px-3"
              type="button"
              @click="toggleDarkMode()">
        <span v-if="mode==='auto'"
              :title="$t('theme.auto')"
              class="icon-[material-symbols--night-sight-auto] dark:text-white text-2xl align-top"/>
        <span v-else-if="mode==='light'"
              :title="$t('theme.light')"
              class="icon-[material-symbols--light-mode] dark:text-white text-2xl align-top"/>
        <span v-else-if="mode==='dark'"
              :title="$t('theme.dark')"
              class="icon-[material-symbols--dark-mode] dark:text-white text-2xl align-top"/>
      </button>
      <div class="inline-block">
        <button class="px-3 peer/language"
                type="button">
          <span aria-hidden="true" class="icon-[material-symbols--language] dark:text-white text-2xl mr-1 align-top"/>
          <span class="dark:text-white">{{ $t("locale.name") }}</span>
        </button>
        <div class="absolute peer-hover/language:block hover:block hidden">
          <!--Placeholder used to as a replace of margin-top to make hover work correctly-->
          <div class="pt-2"/>
          <div class="bg-white dark:bg-black border rounded w-max">
            <ul class="py-2">
              <template v-for="locale in availableLocales">
                <li>
                  <button class="w-full px-4 py-2 text-start dark:text-white"
                          type="button"
                          @click="setLocalePermanently(locale)">{{ getLocalizedLocalName(locale) }}
                  </button>
                </li>
              </template>
            </ul>
          </div>
        </div>
      </div>
      <template v-if="!userInfo">
        <NuxtLink class="px-3 dark:text-white" to="/signup">{{ $t("sign_up") }}</NuxtLink>
        <NuxtLink class="px-3 dark:text-white" to="/signin">{{ $t("sign_in") }}</NuxtLink>
      </template>
      <div v-else class="inline-block">
        <button class="px-3 peer/user"
                type="button">
          <span aria-hidden="true"
                class="icon-[material-symbols--account-circle] dark:text-white text-2xl mr-1 align-top"/>
          <!--          FIXME {{ userInfo.username }} -->
          <NuxtLink :to="`/user`" class="dark:text-white">{{ userInfo.username }}</NuxtLink>
        </button>
        <div class="absolute peer-hover/user:block hover:block hidden">
          <!--Placeholder used to as a replace of margin-top to make hover work correctly-->
          <div class="pt-2"/>
          <div class="bg-white dark:bg-black border rounded w-max">
            <ul class="py-2">
              <li>
                <NuxtLink
                    :to="`/settings`"
                    class="block w-full px-4 py-2 text-start">
                  <span aria-hidden="true"
                        class="icon-[material-symbols--settings] dark:text-white text-2xl mr-1 align-top"/>
                  <span class="dark:text-white">{{ $t("settings") }}</span>
                </NuxtLink>
              </li>
              <li>
                <button class="w-full px-4 py-2 text-start"
                        type="button"
                        @click="signout">
                  <span aria-hidden="true"
                        class="icon-[material-symbols--logout] dark:text-white text-2xl mr-1 align-top"/>
                  <span class="dark:text-white">{{ $t("sign_out") }}</span>
                </button>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="container mx-auto pt-4 pb-2 lg:hidden flex flex-row items-center bg-white dark:bg-black">
    <div>
      <button class="px-3"
              type="button"
              @click="isMenuDrawerOpen=!isMenuDrawerOpen">
        <span :title="$t('menu')" class="icon-[material-symbols--menu] dark:text-white text-2xl align-text-top"></span>
      </button>
    </div>
    <div>
      <NuxtLink class="dark:text-white" to="/">NeraBBS</NuxtLink>
    </div>
  </div>
  <LazyDrawer v-model:is-open="isMenuDrawerOpen">
    <NuxtLink class="block w-full p-5 dark:text-white" to="/" @click="isMenuDrawerOpen=false">NeraBBS</NuxtLink>
    <div class="px-4 flex flex-col items-start gap-3">
      <button class="w-full text-start"
              type="button"
              @click="toggleDarkMode()">
        <template v-if="mode==='auto'">
          <span class="icon-[material-symbols--night-sight-auto] dark:text-white text-xl mr-1 align-sub"/>
          <span class="dark:text-white">{{ $t('theme.auto') }}</span>
        </template>
        <template v-else-if="mode==='light'">
          <span class="icon-[material-symbols--light-mode] dark:text-white text-xl mr-1 align-sub"/>
          <span class="dark:text-white">{{ $t('theme.light') }}</span>
        </template>
        <template v-else-if="mode==='dark'">
          <span class="icon-[material-symbols--dark-mode] dark:text-white text-xl mr-1 align-sub"/>
          <span class="dark:text-white">{{ $t('theme.dark') }}</span>
        </template>
      </button>
      <button class="w-full text-start"
              type="button"
              @click="isMobileLanguageSelectorDrawerOpen=!isMobileLanguageSelectorDrawerOpen">
        <span aria-hidden="true" class="icon-[material-symbols--language] dark:text-white text-xl mr-1 align-sub"/>
        <span class="dark:text-white">{{ $t("locale.name") }}</span>
      </button>
      <template v-if="!userInfo">
        <NuxtLink class="w-full dark:text-white" to="/signup" @click="isMenuDrawerOpen=false">{{
            $t("sign_up")
          }}
        </NuxtLink>
        <NuxtLink class="w-full dark:text-white" to="/signin" @click="isMenuDrawerOpen=false">{{
            $t("sign_in")
          }}
        </NuxtLink>
      </template>
      <button v-else
              class="w-full text-start"
              type="button"
              @click="isMobileUserDrawerOpen=!isMobileUserDrawerOpen">
        <span aria-hidden="true"
              class="icon-[material-symbols--account-circle] dark:text-white text-xl mr-1 align-sub"/>
        <span class="dark:text-white">{{
            userInfo.username
          }}</span>
      </button>
    </div>
  </LazyDrawer>
  <LazyDrawer v-model:is-open="isMobileLanguageSelectorDrawerOpen" :z-index="20">
    <div class="w-full h-full bg-white dark:bg-black">
      <ul class="py-2">
        <template v-for="locale in availableLocales">
          <li>
            <button class="w-full px-4 py-2 text-start dark:text-white"
                    type="button"
                    @click="setLocalePermanently(locale); isMobileLanguageSelectorDrawerOpen=false">
              {{ getLocalizedLocalName(locale) }}
            </button>
          </li>
        </template>
      </ul>
    </div>
  </LazyDrawer>
  <LazyDrawer v-model:is-open="isMobileUserDrawerOpen" :z-index="20">
    <div class="w-full h-full bg-white dark:bg-black">
      <ul class="py-2">
        <li>
          <NuxtLink
              :to="`/user`"
              class="block w-full px-4 py-2 text-start dark:text-white"
              @click="isMobileUserDrawerOpen=false">
            <span aria-hidden="true"
                  class="icon-[material-symbols--account-circle] dark:text-white text-2xl mr-1 align-top"/>
            <span class="dark:text-white">{{ $t("user_center") }}</span>
          </NuxtLink>
        </li>
        <li>
          <NuxtLink
              :to="`/settings`"
              class="block w-full px-4 py-2 text-start dark:text-white"
              @click="isMobileUserDrawerOpen=false">
            <span aria-hidden="true"
                  class="icon-[material-symbols--settings] dark:text-white text-2xl mr-1 align-top"/>
            <span class="dark:text-white">{{ $t("settings") }}</span>
          </NuxtLink>
        </li>
        <li>
          <button class="w-full px-4 py-2 text-start"
                  type="button"
                  @click="signout(); isMobileUserDrawerOpen=false">
            <span aria-hidden="true"
                  class="icon-[material-symbols--logout] dark:text-white text-2xl mr-1 align-top"/>
            <span class="dark:text-white">{{ $t("sign_out") }}</span>
          </button>
        </li>
      </ul>
    </div>
  </LazyDrawer>
</template>
