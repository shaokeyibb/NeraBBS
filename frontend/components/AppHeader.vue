<script lang="ts" setup>
import useLocale from "~/hooks/locale";
import useDarkMode from "~/hooks/dark_mode";

const {$pinia} = useNuxtApp()

const {availableLocales} = useI18n()

const {getLocalizedLocalName, setLocalePermanently} = useLocale()

const {mode, toggleDarkMode} = useDarkMode($pinia)

</script>

<template>
  <div class="container mx-auto p-4 flex flex-row justify-between items-center">
    <div>
      <span class="dark:text-white">NeraBBS</span>
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
      <button class="px-3"
              popovertarget="language-selector"
              type="button">
        <span aria-hidden="true" class="icon-[material-symbols--language] dark:text-white text-2xl mr-1 align-top"/>
        <span class="dark:text-white">{{ $t("locale.name") }}</span>
      </button>
      <button class="px-3 dark:text-white" type="button">{{ $t("sign_up") }}</button>
      <button class="px-3 dark:text-white" type="button">{{ $t("sign_in") }}</button>
    </div>
  </div>
  <dialog id="language-selector"
          class="border rounded -translate-y-1/3"
          popover>
    <ul class="py-2">
      <template v-for="locale in availableLocales">
        <li>
          <button class="w-full px-4 py-2 text-start"
                  popovertarget="language-selector"
                  popovertargetaction="hide"
                  type="button"
                  @click="setLocalePermanently(locale)">{{ getLocalizedLocalName(locale) }}
          </button>
        </li>
      </template>
    </ul>
  </dialog>
</template>