<script lang="ts" setup>
import Post from "~/components/Post.vue"

const route = useRoute()
const id = route.params.id as string
</script>

<template>
  <post :postID="parseInt(id)"/>
</template>