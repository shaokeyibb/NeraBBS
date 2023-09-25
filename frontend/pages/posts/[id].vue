<script lang="ts" setup>
import Posts from "~/components/Posts.vue"
import BackableComponent from "~/components/utils/BackableComponent.vue";

const route = useRoute()
const id = route.params.id as string
</script>

<template>
  <BackableComponent :component="Posts" :postID="id" :title="$t('posts.title')" back-to="/"/>
</template>