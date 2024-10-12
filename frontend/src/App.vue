
<template>
  <div :style="{ 'background-image': 'url(' + defineBackgroundImage() + ')'}"  class="appBackground" >
    <div class="tintedBG">
      <div class="mx-auto text-white m-0 p-0" style="max-width: 1280px;">

        <Navbar v-if="userStore.isAuthenticated && !isHomePage"/>
        <NavbarLandingPage v-else-if="!userStore.isAuthenticated"/>
        
        <RouterView />
  
      </div>
    </div>
  </div>
</template>


<script>
import { RouterView, useRoute  } from 'vue-router'
import Navbar from './components/Navbar/Navbar.vue';
import NavbarLandingPage from './components/Navbar/NavbarLandingPage.vue'
import { useUserStore } from '@/stores/store';

export default{
  name:"App",
  components:{RouterView,Navbar,NavbarLandingPage},
  data(){
    return{
      BACKGOUNDIMAGE:{
        '/':'https://eu-images.contentstack.com/v3/assets/blt740a130ae3c5d529/blt1672290e8640ef07/6568662857c00e040aafe9ce/black_myth_wukong.png?width=1280&auto=webp&quality=95&format=jpg&disable=upscale',
        '/about':'https://cdn.mos.cms.futurecdn.net/TmezMwAfXatkL7M2TDotkM-1200-80.jpg',
        '/auth':'https://mustsharenews.com/wp-content/uploads/2024/08/Black-Myth-Wukong.jpg',
        '/battleroyale':'https://cdn.mos.cms.futurecdn.net/sgFRba5wiRrXrWh2bVkF5a-1920-80.jpg.webp',
        '/clanwar':'https://platform.polygon.com/wp-content/uploads/sites/2/2024/08/black-myth-wukong-secret-ending-guide-8.jpg',
        '/booking':'https://static1.thegamerimages.com/wordpress/wp-content/uploads/wm/2024/09/8-wukong-and-journey-to-the-west-1.jpg',
        '/Clan':'',
        '/leaderboard':'https://i0.wp.com/theguidehall.com/wp-content/uploads/2024/08/Black-Myth_-Wukong_20240823164851.jpg',
        '/profile':'https://images5.alphacoders.com/112/1129255.jpg',
        '/admin':'https://gamingbolt.com/wp-content/uploads/2024/08/Black-Myth-Wukong-Featured-Image.jpg',
        
      },
      isLogin: true,
    }
  },
  computed:{
    currentRoute() {
      const route = useRoute();
      return route.path; // returns the current route path
    },
    isHomePage(){
      const route = useRoute();
      return route.path == '/'; 
    }
  },
  methods:{
    defineBackgroundImage(){
      const route = useRoute();
      return this.BACKGOUNDIMAGE[route.path] || '';
    }
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }
}

</script>

<style scoped>
.appBackground {
  min-height: 100vh;
  background-position: top;
  background-repeat: no-repeat;
  background-size: cover;
}
.tintedBG{
  background-color: rgba(0, 0, 0, 0.4);
  background-size: cover;
  min-height: 100vh;
}
</style>
