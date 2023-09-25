<script lang="ts" setup>
import Profile from "~/components/settings/Profile.vue"
import BackableComponent from "~/components/utils/BackableComponent.vue";
</script>

<template>
  <BackableComponent :component="Profile" :title="$t('settings.profile.title')" back-to="/settings"/>
</template>