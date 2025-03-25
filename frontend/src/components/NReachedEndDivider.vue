<script setup lang="ts">
import NText from "./NText.vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
</script>

<template>
  <div class="reached-end">
    <mdui-divider class="reached-end--divider" />
    <NText>
      {{ t("reached_end") }}
    </NText>
  </div>
</template>

<style scoped>
.reached-end {
  width: 30%;

  align-self: center;

  text-align: center;
}

.reached-end--divider {
  margin: 12px 0;
}
</style>
