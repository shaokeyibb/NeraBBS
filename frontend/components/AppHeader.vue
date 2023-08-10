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
</script>

<template>
  <div class="container mx-auto p-4 flex flex-row justify-between items-center">
    <div>
      <NuxtLink class="dark:text-white" to="/">NeraBBS</NuxtLink>
    </div>
    <div>
      <button class="px-3"
              type="button"
              @click="toggleDarkMode()">
        <span v-if="mode==='auto'"
              :title="$t('theme.auto')"
              class="icon-[material-symbols--night-sight-auto] dark:text-white text-2xl mr-1 align-top"/>
        <span v-else-if="mode==='light'"
              :title="$t('theme.light')"
              class="icon-[material-symbols--light-mode] dark:text-white text-2xl mr-1 align-top"/>
        <span v-else-if="mode==='dark'"
              :title="$t('theme.dark')"
              class="icon-[material-symbols--dark-mode] dark:text-white text-2xl mr-1 align-top"/>
      </button>
      <button class="relative px-3 group"
              type="button">
        <span aria-hidden="true" class="icon-[material-symbols--language] dark:text-white text-2xl mr-1 align-top"/>
        <span class="dark:text-white">{{ $t("locale.name") }}</span>
        <ClientOnly>
          <div class="absolute group-hover:block hidden">
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
        </ClientOnly>
      </button>
      <template v-if="!userInfo">
        <NuxtLink class="px-3 dark:text-white" to="/signup">{{ $t("sign_up") }}</NuxtLink>
        <NuxtLink class="px-3 dark:text-white" to="/signin">{{ $t("sign_in") }}</NuxtLink>
      </template>
      <button v-else
              class="relative px-3 group"
              type="button">
        <span aria-hidden="true"
              class="icon-[material-symbols--account-circle] dark:text-white text-2xl mr-1 align-top"/>
        <NuxtLink :to="`/user`" class="dark:text-white">{{ userInfo.username }}</NuxtLink>
        <ClientOnly>
          <div class="absolute group-hover:block hidden">
            <!--Placeholder used to as a replace of margin-top to make hover work correctly-->
            <div class="pt-2"/>
            <div class="bg-white dark:bg-black border rounded w-max">
              <ul class="py-2">
                <li>
                  <button class="w-full px-4 py-2 text-start dark:text-white"
                          type="button"
                          @click="signout">{{ $t("sign_out") }}
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </ClientOnly>
      </button>
    </div>
  </div>
</template>
