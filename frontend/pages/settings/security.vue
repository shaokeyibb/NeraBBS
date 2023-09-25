<script lang="ts" setup>
import Security from "~/components/settings/Security.vue"
import BackableComponent from "~/components/utils/BackableComponent.vue";
</script>

<template>
  <BackableComponent :component="Security" :title="$t('settings.security.title')" back-to="/settings"/>
</template>