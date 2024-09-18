<template>
  <nav class="mx-4 py-2">
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

</style>