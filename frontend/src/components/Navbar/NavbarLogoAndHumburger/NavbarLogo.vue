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
      return this.isLogin ? "/" : "/auth"
    }
  },
  methods:{
    // checkScreenSize() {
    //   this.isLargeScreen = window.innerWidth > 992;
    //   console.log(this.isLargeScreen)
    // },
  },
}
</script>

<style scoped>

.logo{
    width: 100px;
}
</style>
