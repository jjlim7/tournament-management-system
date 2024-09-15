<script setup>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar/Navbar.vue';
import NavbarLandingPage from './components/Navbar/NavbarLandingPage.vue'
</script>

<template>
  <div class="background text-white m-0 p-0">
    <!-- <NavbarLandingPage/> -->
    <Navbar/>
    <RouterView />
  </div>
</template>



<style scoped>
.background {
  height: 100vh; /* Full viewport height */
  background: linear-gradient(to bottom,#000000,#424242 );
}
</style>
