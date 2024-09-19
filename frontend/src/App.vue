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
  <div class="maxWidth mx-auto text-white m-0 p-0">
    <NavbarLandingPage/>
    <!-- <Navbar/> -->
    <RouterView />
  </div>
</template>



<style>

</style>
