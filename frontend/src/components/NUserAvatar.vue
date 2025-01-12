<script setup lang="ts">
import type { UserProfile } from "../types/backend.ts";

const { user } = defineProps<{
  user?: UserProfile;
}>();
</script>

<template>
  <div>
    <template v-if="user !== undefined">
      <mdui-avatar v-if="user.avatarPath" :src="user.avatarPath" />
      <mdui-avatar v-else-if="user.username">
        {{ user.username[0] }}
      </mdui-avatar>
      <mdui-avatar v-else>
        {{ user.userID }}
      </mdui-avatar>
    </template>
    <mdui-avatar v-else icon="account_circle" />
  </div>
</template>

<style scoped></style>
