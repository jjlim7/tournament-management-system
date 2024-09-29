<template>
  <div class="blurred-bg-card rounded-4">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name:"BlurredBGCard"
}
</script>

<style scoped>
.blurred-bg-card {
  border: 1px solid rgb(250, 144, 33, 0.4); /* Orange border */
  background: rgba(255, 255, 255, 0.1); /* Semi-transparent background */
  backdrop-filter: blur(10px); /* Blurring effect */
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); /* shadow effect */
}
</style>