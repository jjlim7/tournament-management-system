<template>
  <nav class="px-4 bg-dark backgoundColour py-2 ">
    <div class="d-flex justify-content-between align-items-center"  >
      <NavbarLogo /> 
      <button class="btn btn-primary fw-bold text-white" @click="navigateToAuth">Login / Register</button>
    </div>
  </nav>
</template>

<script>
import NavbarLogo from './NavbarLogoAndHumburger/NavbarLogo.vue';

export default {
  name: 'Navbar',
  components:{ NavbarLogo },
  data(){
    return{
      islogin: false,
    }
  },
  methods:{
    navigateToAuth(){
      this.$router.push('/auth');
    }
  }
};
</script>

<style scoped>
.backgoundColour{
  background-color: rgba(0, 0, 0, 0.3);
  box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); 
}
</style>