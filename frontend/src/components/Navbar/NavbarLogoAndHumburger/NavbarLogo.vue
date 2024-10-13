<template>
  <div class="text-center">
    <RouterLink class="navbar-brand p-0 m-0" :to="navlink">
      <li style="list-style: none">
        <img src="https://promotions.lenovo.com/uploads/assets/f848702b3458c766a8af753da6d227d6.png" alt="" class="logo">
      </li>
    </RouterLink>
  </div>
</template>

<script>
import { RouterLink } from 'vue-router';
import { useUserStore } from '@/stores/store';

export default {
  name: 'NavbarLogo',
  components:{
    RouterLink
  },
  data(){return{
      isLogin: true,
  }},
  computed:{
    navlink(){
      if(this.userStore.isAuthenticated){
        return "/";
      }
      else{
        return "/about"
      }
    }
  },
  methods:{
    // checkScreenSize() {
    //   this.isLargeScreen = window.innerWidth > 992;
    //   console.log(this.isLargeScreen)
    // },
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }
}
</script>

<style scoped>

.logo{
    width: 100px;
}
</style>
