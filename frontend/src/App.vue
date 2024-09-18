<script>
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar/Navbar.vue';
import NavbarLandingPage from './components/Navbar/NavbarLandingPage.vue'
import BlurredBGCard from './components/Cards/BlurredBGCard.vue';
import RankProgress from './components/RankProgress/RankProgress.vue';

export default{
  name:"App",
  components:{RouterView,Navbar,NavbarLandingPage,BlurredBGCard},
  methods:{
    
  }
}

</script>

<template>
  <div class="maxWidth mx-auto text-white m-0 p-0">
    <!-- <NavbarLandingPage/> -->
    <Navbar/>
    <RouterView />
  </div>
</template>



<style lang="scss">
.maxWidth{
  max-width: 1280px;
  background-image: url('https://png.pngtree.com/thumb_back/fh260/background/20240808/pngtree-river-in-forest-landscape-with-rocks-image_16125578.jpg');
  height: 100vh;
}
</style>
