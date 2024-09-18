<script>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar/Navbar.vue';
import NavbarLandingPage from './components/Navbar/NavbarLandingPage.vue'

export default{
  name:"App",
  components:{RouterView,Navbar,NavbarLandingPage},
  methods:{
    
  }
}

</script>

<template>
  <div class="mx-auto text-white m-0 p-0" style="max-width: 1280px;">
    <!-- <NavbarLandingPage/> -->
    <Navbar/>
    <RouterView />
  </div>
</template>



<style>

</style>
