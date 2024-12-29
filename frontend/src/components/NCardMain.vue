<script lang="ts" setup>
import NText from "./NText.vue";

const {title, subhead, supportingText} = defineProps<{
  title?: string;
  subhead?: string;
  supportingText?: string;
}>();

const {default: _default, actions} = defineSlots<{
  default: unknown; // will not be shown when supportingText is present
  actions: unknown;
}>();
</script>

<template>
  <div class="card--content">
    <div v-if="title || subhead" class="card--content--headline">
      <NText v-if="title" class="title" scale="body" size="large">{{
          title
        }}
      </NText>
      <NText v-if="subhead" class="subhead" scale="body" size="medium">{{
          subhead
        }}
      </NText>
    </div>
    <div v-if="supportingText || _default" class="card--content--supporting">
      <NText v-if="supportingText" class="supporting" scale="body"
      >{{ supportingText }}
      </NText>
      <slot v-else-if="_default" name="default"/>
    </div>
    <div v-if="actions" class="card--content--actions">
      <slot name="actions"/>
    </div>
  </div>
</template>

<style scoped>
.card--content {
  display: flex;
  flex-direction: column;

  gap: 32px;

  padding: 16px;
}

.card--content--headline {
  display: flex;
  flex-direction: column;
}

.card--content--actions {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;

  gap: 8px;
}
</style>
