<script setup>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar/Navbar.vue';
import NavbarLandingPage from './components/Navbar/NavbarLandingPage.vue'
</script>

<template>
  <div class="maxWidth mx-auto text-white m-0 p-0">
    <NavbarLandingPage/>
    <!-- <Navbar/> -->
    <RouterView />
  </div>
</template>



<style scoped>
.maxWidth{
  max-width: 1280px;
}
</style>
